package com.chy.xxx.ms.aop.operationlog;

import com.chy.xxx.ms.exception.RtBizAssert;
import com.chy.xxx.ms.modules.system.dao.SysUserLogDao;
import com.chy.xxx.ms.modules.system.po.SysUserLogPo;
import com.chy.xxx.ms.request.RequestContext;
import com.chy.xxx.ms.request.RequestContextHolder;
import com.chy.xxx.ms.util.AopUtil;
import com.chy.xxx.ms.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.function.BiPredicate;


/***
 * 操作日志切面
 *
 * @author chy
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Resource
    private SysUserLogDao sysUserLogDao;

    /**
     * 最大文本长度
     */
    private static final int MAX_TEXT_LENGTH = 65535;

    /**
     * 无需保存到 t_sys_user_log 的请求参数断言
     */
    private static final BiPredicate<Parameter, Object> NOT_LOG_REQUEST_PARAM_PREDICATE = (parameter, value) ->
            value instanceof ServletRequest
                    || value instanceof ServletResponse
                    || value instanceof HttpSession
                    || value instanceof MultipartFile;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.chy.xxx.ms.aop.operationlog.OperationLog)")
    public void pointCut() {
        //@OperationLog切入点
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObj = null;
        Throwable throwable = null;
        try {
            returnObj = joinPoint.proceed();
        } catch (Throwable te) {
            throwable = te;
            throw throwable;
        } finally {
            //将操作日志持久化到db，catch住，发生异常时不影响接口数据返回
            try {
                this.sysUserLog(joinPoint, returnObj, throwable);
            } catch (Exception e) {
                log.error("记录操作记录执行异常", e);
            }
        }
        return returnObj;
    }

    private void sysUserLog(ProceedingJoinPoint joinPoint, Object returnObj, Throwable throwable) throws NoSuchMethodException {
        Method sourceMethod = AopUtil.getSourceMethod(joinPoint);
        OperationLog operationLog = AnnotationUtils.getAnnotation(sourceMethod, OperationLog.class);
        RtBizAssert.assertNotNull(operationLog, "获取@OperationLog注解为空，sourceMethod=" + joinPoint.getSignature());

        Map<String, Object> methodArgsMap = AopUtil.getMethodArgsMap(joinPoint, NOT_LOG_REQUEST_PARAM_PREDICATE);
        String reqParam = this.buildJsonData(methodArgsMap);
        String respData = StringUtils.EMPTY;
        if (operationLog.saveRespData()) {
            //接口发生异常时则记录异常信息
            respData = throwable == null ? this.buildJsonData(returnObj) : "执行异常：" + throwable;
        }

        RequestContext requestContext = RequestContextHolder.getRequestContext();
        SysUserLogPo sysUserLogPo = SysUserLogPo.builder()
                .logTitle(operationLog.logTitle())
                .username(requestContext.getUsername())
                .realName(requestContext.getRealName())
                .reqIp(requestContext.getReqIp())
                .userAgent(requestContext.getUserAgent())
                .reqParam(reqParam)
                .respData(respData)
                .build();
        sysUserLogDao.insert(sysUserLogPo);
    }

    private String buildJsonData(Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }
        String jsonStr = JacksonUtil.toJsonStr(obj);
        //超过最大长度则截取
        if (jsonStr.length() > MAX_TEXT_LENGTH) {
            jsonStr = jsonStr.substring(0, MAX_TEXT_LENGTH);
        }
        return jsonStr;
    }

}
