package com.chy.xxx.ms.security;

import com.chy.xxx.ms.modules.system.enums.SysResourceTypeEnum;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysResourceQo;
import com.chy.xxx.ms.modules.system.service.db.SysResourceDbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态权限数据源，获取当前请求资源的访问规则
 *
 * @author chy
 */
@Slf4j
@Component
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, CommandLineRunner {

    /**
     * 存储所有接口资源的访问规则
     */
    private static final Map<String, ConfigAttribute> CONFIG_ATTRIBUTE_MAP = new ConcurrentHashMap<>();

    @Resource
    private SysResourceDbService sysResourceDbService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取当前请求的请求方式、接口路径
        FilterInvocation filterInvocation = (FilterInvocation) o;
        String requestMethod = filterInvocation.getRequest().getMethod();
        String path = URI.create(filterInvocation.getRequestUrl()).getPath();

        //获取访问当前请求资源的所有访问规则。eg.查询用户信息/v1/system/users/info，可能定义了多个访问规则：/v1/system/users/**、/v1/system/**
        List<ConfigAttribute> configAttributes = new ArrayList<>();
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String pattern : CONFIG_ATTRIBUTE_MAP.keySet()) {
            //请求方式、url均匹配
            String[] split = pattern.split(MsSecurityConstant.RES_SEPARATOR);
            if (StringUtils.equalsIgnoreCase(split[0], requestMethod) && pathMatcher.match(split[1], path)) {
                configAttributes.add(CONFIG_ATTRIBUTE_MAP.get(pattern));
            }
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return Collections.emptyList();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void run(String... args) throws Exception {
        //服务启动后自动加载系统接口资源
        log.info("开始加载系统接口资源的访问规则...");
        List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                .resType(SysResourceTypeEnum.INTERFACE.getType())
                .build());
        for (SysResourcePo sysResourcePo : sysResourcePos) {
            String key = sysResourcePo.getResReqMethod() + MsSecurityConstant.RES_SEPARATOR + sysResourcePo.getResUrl();
            ConfigAttribute configAttribute = new SecurityConfig(sysResourcePo.getId() + MsSecurityConstant.RES_SEPARATOR + sysResourcePo.getResName());
            CONFIG_ATTRIBUTE_MAP.put(key, configAttribute);
        }
        log.info("系统接口资源的访问规则加载完毕,共计{}个", CONFIG_ATTRIBUTE_MAP.size());
    }

}