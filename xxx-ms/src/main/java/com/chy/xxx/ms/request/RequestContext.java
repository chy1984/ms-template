package com.chy.xxx.ms.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求上下文
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext implements Serializable {

    private static final long serialVersionUID = -5702298791853401585L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 请求者ip
     */
    private String reqIp;

    /**
     * 用户代理
     */
    private String userAgent;

}
