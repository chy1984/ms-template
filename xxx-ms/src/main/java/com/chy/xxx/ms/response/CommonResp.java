package com.chy.xxx.ms.response;

import com.chy.xxx.ms.enums.ErrorCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 通用响应结果封装，强制绑定 IErrorCode
 *
 * @author chy
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel("通用响应结果")
public class CommonResp<T> implements Serializable {

    private static final long serialVersionUID = -8229433643410764351L;

    /**
     * 响应码
     *
     * @see IErrorCode
     */
    @ApiModelProperty("响应码：000000 操作成功，其余为失败")
    private String code;

    /**
     * 提示信息
     */
    @ApiModelProperty("提示信息")
    private String message;

    /**
     * 响应数据
     */
    @ApiModelProperty("响应数据")
    private T data;

    public static <T> CommonResp<T> fail(IErrorCode errorCode, T data) {
        return new CommonResp<>(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static CommonResp<Void> fail(IErrorCode errorCode) {
        return fail(errorCode, null);
    }

    public static <T> CommonResp<T> success(T data) {
        return fail(ErrorCodeEnum.SUCCESS, data);
    }

    public static CommonResp<Void> success() {
        return fail(ErrorCodeEnum.SUCCESS);
    }

    /**
     * 如果是后台之类内部人员使用的系统，可以适当返回具体错误信息
     */
    public static CommonResp<String> paramError(String detailMessage) {
        return fail(ErrorCodeEnum.PARAM_ERROR, detailMessage);
    }

    public static CommonResp<Void> paramError() {
        return fail(ErrorCodeEnum.PARAM_ERROR);
    }

    public static CommonResp<Void> unauthorized() {
        return fail(ErrorCodeEnum.UNAUTHORIZED);
    }

    public static CommonResp<Void> forbidden() {
        return fail(ErrorCodeEnum.FORBIDDEN);
    }

    public static <T> CommonResp<T> notImplemented() {
        return fail(ErrorCodeEnum.NOT_IMPLEMENTED, null);
    }

    public static CommonResp<Void> unknownError() {
        return fail(ErrorCodeEnum.UNKNOWN_ERROR);
    }

}
