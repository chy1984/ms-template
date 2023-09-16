package com.chy.xxx.ms.modules.system.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统用户token响应数据
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统用户token响应数据")
public class SysUserTokenRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户登录凭证")
    private String token;

    @ApiModelProperty(value = "传递token给服务端时，需要添加的token前缀")
    private String tokenPrefix;

}
