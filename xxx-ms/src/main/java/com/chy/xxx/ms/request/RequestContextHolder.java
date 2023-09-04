package com.chy.xxx.ms.request;

/**
 * 请求上下文助手类
 *
 * @author chy
 */
public class RequestContextHolder {

    /**
     * 请求上下文
     */
    private static final ThreadLocal<RequestContext> REQUEST_CONTEXT = new ThreadLocal<>();

    /**
     * 获取请求上下文<br/>
     * 说明：不要使用 ThreadLocal#get 获取RequestContext，可能上次清空后还没初始化RequestContext，统一使用此方法获取RequestContext
     *
     * @return RequestContext
     */
    public static RequestContext getRequestContext() {
        if (REQUEST_CONTEXT.get() == null) {
            REQUEST_CONTEXT.set(new RequestContext());
        }
        return REQUEST_CONTEXT.get();
    }

    /**
     * 清空请求上下文
     */
    public static void clear() {
        REQUEST_CONTEXT.remove();
    }

    /**
     * 设置用户信息
     *
     * @param username 用户名
     * @param realName 真实姓名
     */
    public static void setUserInfo(String username, String realName) {
        RequestContext requestContext = getRequestContext();
        requestContext.setUsername(username);
        requestContext.setRealName(realName);
    }

    /**
     * 设置客户端信息
     *
     * @param reqIp     请求者ip
     * @param userAgent 用户代理
     */
    public static void setClientInfo(String reqIp, String userAgent) {
        RequestContext requestContext = getRequestContext();
        requestContext.setReqIp(reqIp);
        requestContext.setUserAgent(userAgent);
    }

    private RequestContextHolder() {
    }

}
