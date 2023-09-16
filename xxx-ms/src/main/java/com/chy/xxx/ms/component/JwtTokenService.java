package com.chy.xxx.ms.component;

import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.properties.JwtTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
     * 从请求中获取token
     *
     * @param request http请求
     * @return String
     */
    public String getToken(HttpServletRequest request) {
        String tokenHeaderValue = request.getHeader(jwtTokenProperties.getRequestHeader());
        if (StringUtils.isBlank(tokenHeaderValue) || !tokenHeaderValue.startsWith(jwtTokenProperties.getPrefix())) {
            return null;
        }
        return tokenHeaderValue.substring(jwtTokenProperties.getPrefix().length());
    }


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
        Pair<Boolean, Claims> pair = this.needRefreshToken(token);
        //需要时才刷新，否则继续使用当前token
        if (Boolean.TRUE.equals(pair.getLeft())) {
            Claims claims = pair.getRight();
            claims.put(CLAIM_KEY_CREATE_TIME, new Date());
            return this.generateToken(claims);
        }
        return token;
    }

    public Pair<Boolean, Claims> needRefreshToken(String token) {
        Claims claims = this.getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        //即将过期时才刷新token
        Date thresholdTime = DateUtils.addSeconds(expiration, -jwtTokenProperties.getRefreshBeforeExpire());
        Date now = new Date();
        boolean needRefreshToken = now.after(thresholdTime) && now.before(expiration);
        return Pair.of(needRefreshToken, claims);
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
        return Jwts.parser()
                .setSigningKey(jwtTokenProperties.getSecret())
                //此处可能会抛出异常：token不合法、已过期
                .parseClaimsJws(token)
                .getBody();
    }

}
