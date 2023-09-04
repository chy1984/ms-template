package com.chy.xxx.ms.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 *
 * @author chy
 */
public class IpUtil {

    private static final String UNKNOWN = "unknown";

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return StringUtils.EMPTY;
        }

        String ip = request.getHeader("x-original-forwarded-for");
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotBlank(ip)) {
                //多次反向代理后会有多个ip值，第一个才是真实ip
                int index = ip.indexOf(",");
                return index == -1 ? ip : ip.substring(0, index);
            }
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.trimToEmpty(ip);
    }

    private IpUtil() {
    }

}
