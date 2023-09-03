package com.chy.xxx.ms.response;

/**
 * 错误码接口，各服务可实现此接口来定义自己的错误码
 *
 * @author chy
 */
public interface IErrorCode {

    /**
     * 获取错误码
     *
     * @return String
     */
    String getCode();

    /**
     * 获取提示信息，可能展示给用户，文本需要用户友好
     *
     * @return String
     */
    String getMessage();

}
