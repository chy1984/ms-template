package com.chy.xxx.ms.enums;

import com.chy.xxx.ms.response.IErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 后台错误码枚举<br/>
 * 以前2位区分业务模块，保留0、9开头的作为项目通用错误码，0成功，9失败
 *
 * @author chy
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MsErrorCodeEnum implements IErrorCode {

    /************************** 通用错误码，成功以0开头，失败以9开头 ************************/
    SUCCESS("000000", "操作成功"),
    PARAM_ERROR("900000", "参数错误"),
    UNAUTHORIZED("900001", "未登录或token不合法"),
    FORBIDDEN("900002", "没有访问权限"),
    NOT_IMPLEMENTED("900003", "接口暂未实现"),
    UNKNOWN_ERROR("999999", "系统繁忙，请稍后再试"),

    /************************** 业务错误码-系统管理，以10开头 ****************************/
    SYS_USER_NOT_EXIST("100000", "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR("100001", "用户名或密码错误"),
    SYS_USER_DISABLED("100002", "账号已被禁用"),
    USERNAME_ALREADY_EXIST("100003", "用户名已存在"),
    OLD_PASSWORD_ERROR("100004", "原密码错误"),
    SYS_ROLE_NOT_EXIST("100005", "角色不存在"),
    ROLE_NAME_ALREADY_EXIST("100006", "角色名已存在"),
    RESOURCE_REQ_METHOD_AND_URL_ALREADY_EXIST("100007", "资源请求方式、url已存在"),
    SYS_RESOURCE_NOT_EXIST("100008", "资源不存在"),
    CANNOT_DELETE_RESOURCE_WITH_SUB_RESOURCES("100009", "该资源下挂有子资源，不能被删除"),
    PARENT_RESOURCE_MAPPING_ERROR("100010", "父子资源关联关系不合法"),

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
