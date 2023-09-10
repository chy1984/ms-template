package com.chy.xxx.ms.util;

import com.chy.xxx.ms.response.CommonResp;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * http工具类
 *
 * @author chy
 */
public class HttpUtil {

    /**
     * 直接返回http响应<br/>
     * filter、interceptor等controller外的异常不会被全局异常处理器捕获处理，可以自行catch后调用此方法直接返回异常响应信息
     *
     * @param response   http响应
     * @param commonResp 应用通用响应数据
     * @param <T>        响应数据
     * @throws IOException IO异常
     */
    public static <T> void flushResponse(HttpServletResponse response, CommonResp<T> commonResp) throws IOException {
        //http状态码依然返回200，前端统一通过响应体中的错误码判断结果
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JacksonUtil.toJsonStr(commonResp));
        response.getWriter().flush();
    }

    private HttpUtil() {
    }

}
