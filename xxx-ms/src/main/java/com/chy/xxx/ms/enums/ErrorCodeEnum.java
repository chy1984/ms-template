package com.chy.xxx.ms.enums;

import com.chy.xxx.ms.response.IErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 错误码枚举<br/>
 * 以前2位区分业务模块，保留0、9开头的作为项目通用错误码，0成功，9失败
 *
 * @author chy
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCodeEnum implements IErrorCode {

    /************************** 通用错误码，成功以0开头，失败以9开头 ************************/
    SUCCESS("000000", "操作成功"),
    PARAM_ERROR("900000", "参数错误"),
    UNAUTHORIZED("900001", "未登录或token不合法"),
    FORBIDDEN("900002", "没有访问权限"),
    NOT_IMPLEMENTED("900003", "接口暂未实现"),
    UNKNOWN_ERROR("999999", "系统繁忙，请稍后再试"),

    ;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 提示信息，可能展示给用户，文本需要用户友好
     */
    private final String message;

}
