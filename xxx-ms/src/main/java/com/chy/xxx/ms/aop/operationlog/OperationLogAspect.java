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
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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
     * 请求参数无需记录操作日志的类型列表
     */
    private static final List<Class<?>> REQ_ARGS_NOT_LOG_CLASS_LIST = Arrays.asList(
            ServletRequest.class, ServletResponse.class, HttpSession.class, MultipartFile.class
    );

    /**
     * 最大文本长度
     */
    private static final int MAX_TEXT_LENGTH = 65535;

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
            log.info("请求参数args={}", JacksonUtil.toJsonStr(joinPoint.getArgs()));
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
        RtBizAssert.assertNotNull(operationLog, "获取@OperationLog注解为空,sourceMethod=" + joinPoint.getSignature());

        //指定类型的参数不记录
        Map<String, Object> methodArgsMap = AopUtil.getMethodArgsMap(joinPoint, (parameter, value) -> REQ_ARGS_NOT_LOG_CLASS_LIST.contains(parameter.getClass()));
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
