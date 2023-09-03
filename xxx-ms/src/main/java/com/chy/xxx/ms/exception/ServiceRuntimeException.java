package com.chy.xxx.ms.exception;

import com.chy.xxx.ms.response.IErrorCode;

import javax.annotation.Nullable;

/**
 * 运行时服务异常<br/>
 * IErrorCode用于接口返回具体的业务错误码、提示信息；detailMessage用于记录具体异常信息、便于排查问题。<br/>
 * <li>如果接口需要返回具体的业务错误码提示用户：传入IErrorCode参数，全局异常处理器会取IErrorCode对的code、message作为接口响应；</li>
 * <li>如果接口只需返回“999999 系统繁忙”之类的笼统错误信息：使用detailMessage即可，无需传入IErrorCode。</li>
 *
 * @author chy
 */
public class ServiceRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -2518197561535931153L;

    /**
     * 绑定的错误码枚举实例
     */
    private IErrorCode errorCode;

    public ServiceRuntimeException(IErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public ServiceRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public ServiceRuntimeException(IErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public ServiceRuntimeException(IErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public ServiceRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ServiceRuntimeException(IErrorCode errorCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorCode = errorCode;
    }

    @Nullable
    public IErrorCode getErrorCode() {
        return this.errorCode;
    }

}

