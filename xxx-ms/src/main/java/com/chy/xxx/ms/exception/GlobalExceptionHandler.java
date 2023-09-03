package com.chy.xxx.ms.exception;

import com.chy.xxx.ms.response.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author chy
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    public CommonResp<String> handle(BindException be) {
        log.error("捕获绑定异常", be);
        //如果是后台之类内部人员使用的系统，可以适当返回具体错误信息
        BindingResult bindingResult = be.getBindingResult();
        String detailMessage = StringUtils.EMPTY;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                detailMessage = fieldError.getDefaultMessage();
            }
        }
        return CommonResp.paramError(detailMessage);
    }

    @ExceptionHandler(ServiceException.class)
    public CommonResp<Void> handle(ServiceException se) {
        log.error("捕获服务异常,errorCode={}", se.getErrorCode(), se);
        if (se.getErrorCode() != null) {
            return CommonResp.fail(se.getErrorCode());
        }
        return CommonResp.unknownError();
    }

    @ExceptionHandler(ServiceRuntimeException.class)
    public CommonResp<Void> handle(ServiceRuntimeException sre) {
        log.error("捕获运行时服务异常,errorCode={}", sre.getErrorCode(), sre);
        if (sre.getErrorCode() != null) {
            return CommonResp.fail(sre.getErrorCode());
        }
        return CommonResp.unknownError();
    }

    @ExceptionHandler(Exception.class)
    public CommonResp<Void> handle(Exception e) {
        log.error("捕获异常", e);
        return CommonResp.unknownError();
    }

}
