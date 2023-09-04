package com.chy.xxx.ms.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * AOP工具类
 *
 * @author chy
 */
public class AopUtil {

    /**
     * 获取切入点对应的方法
     */
    public static Method getSourceMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
    }

    /**
     * 获取切入点对应方法的返回值类型
     */
    public static Class<?> getMethodReturnType(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getReturnType();
    }

    /**
     * 获取切入点对应方法的方法名
     */
    public static String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    /**
     * 获取切入点对应的方法的参数map
     *
     * @param joinPoint 切入点
     * @return Map<String, Object>，key是参数名，value是对应的实参值
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> getMethodArgsMap(JoinPoint joinPoint) throws NoSuchMethodException {
        return AopUtil.getMethodArgsMap(joinPoint, null);
    }

    /**
     * 获取切入点对应的方法的参数map
     *
     * @param joinPoint       切入点
     * @param notPutPredicate notPut断言，满足断言的参数不会被添加到结果map中。Parameter是参数信息，Object是参数值
     * @return Map<String, Object>，key是参数名，value是对应的实参值
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> getMethodArgsMap(JoinPoint joinPoint, @Nullable BiPredicate<Parameter, Object> notPutPredicate) throws NoSuchMethodException {
        Parameter[] parameters = AopUtil.getSourceMethod(joinPoint).getParameters();
        Object[] args = joinPoint.getArgs();

        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            if (notPutPredicate != null && notPutPredicate.test(parameter, arg)) {
                continue;
            }
            map.put(parameter.getName(), arg);
        }
        return map;
    }

    private AopUtil() {
    }

}
