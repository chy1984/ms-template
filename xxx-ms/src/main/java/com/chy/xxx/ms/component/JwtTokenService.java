package com.chy.xxx.ms.component;

import com.chy.xxx.ms.enums.ErrorCodeEnum;
import com.chy.xxx.ms.exception.ServiceRuntimeException;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.properties.JwtTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt token服务
 *
 * @author chy
 */
@Slf4j
@Component
public class JwtTokenService {

    @Resource
    private JwtTokenProperties jwtTokenProperties;

    /**
     * claims中的用户名字段
     */
    private static final String CLAIM_KEY_USERNAME = "sub";

    /**
     * claims中的真实姓名字段
     */
    private static final String CLAIM_KEY_REAL_NAME = "realName";

    /**
     * claims中的创建时间字段
     */
    private static final String CLAIM_KEY_CREATE_TIME = "created";

    /**
     * 从token中获取用户信息
     *
     * @param token 用户登录凭证
     * @return SysUserPo
     */
    public SysUserPo getUserInfoFromToken(String token) {
        Claims claims = this.getClaimsFromToken(token);
        return SysUserPo.builder()
                .username(claims.getSubject())
                .realName(claims.get(CLAIM_KEY_REAL_NAME, String.class))
                .build();
    }

    /**
     * 校验用户token
     *
     * @param token       http请求传递的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = this.getUserInfoFromToken(token).getUsername();
        return userDetails.getUsername().equals(username);
    }

    /**
     * 根据用户信息生成token
     *
     * @param sysUserPo 用户信息
     * @return String
     */
    public String generateToken(SysUserPo sysUserPo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, sysUserPo.getUsername());
        claims.put(CLAIM_KEY_REAL_NAME, sysUserPo.getRealName());
        claims.put(CLAIM_KEY_CREATE_TIME, new Date());
        return this.generateToken(claims);
    }

    /**
     * 刷新token
     *
     * @param token 当前使用的token
     * @return String
     */
    public String refreshToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //此处未抛出异常，说明必然在token有效期内
        Claims claims = this.getClaimsFromToken(token);
        //达到刷新时间范围内时才刷新token，否则继续使用当前token
        Date thresholdTime = DateUtils.addSeconds(claims.getExpiration(), -jwtTokenProperties.getRefreshBeforeExpire());
        Date nowTime = new Date();
        if (nowTime.after(thresholdTime)) {
            return token;
        }
        claims.put(CLAIM_KEY_CREATE_TIME, nowTime);
        return this.generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        Date expiredTime = new Date(System.currentTimeMillis() + jwtTokenProperties.getExpiration() * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, jwtTokenProperties.getSecret())
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtTokenProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            //token格式错误、信息错误、已过期均抛出异常
            throw new ServiceRuntimeException(ErrorCodeEnum.UNAUTHORIZED, e);
        }
    }

}
