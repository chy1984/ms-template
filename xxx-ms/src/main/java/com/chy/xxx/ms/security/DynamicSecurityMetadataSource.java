package com.chy.xxx.ms.security;

import com.chy.xxx.ms.modules.system.enums.SysResourceStatusEnum;
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
    private static final Map<String, ConfigAttribute> ALL_CONFIG_ATTRIBUTE_MAP = new ConcurrentHashMap<>();

    @Resource
    private SysResourceDbService sysResourceDbService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取当前请求的请求方式、接口路径
        FilterInvocation filterInvocation = (FilterInvocation) o;
        String requestMethod = filterInvocation.getRequest().getMethod();
        //获取的是接口路径，会自动剔除项目路径、get请求?的查询字符串
        String path = URI.create(filterInvocation.getRequestUrl()).getPath();

        //获取当前资源所支持/匹配的访问规则，这些规则是or的关系，用户满足其中1个即可
        List<ConfigAttribute> configAttributes = new ArrayList<>();
        PathMatcher pathMatcher = new AntPathMatcher();
        for (Map.Entry<String, ConfigAttribute> entry : ALL_CONFIG_ATTRIBUTE_MAP.entrySet()) {
            //请求方式、url均匹配，url支持通配符*、**
            String[] split = StringUtils.split(entry.getKey(), MsSecurityConstant.RES_REQ_METHOD_URL_SEPARATOR);
            if (StringUtils.equalsIgnoreCase(split[0], requestMethod) && pathMatcher.match(split[1], path)) {
                configAttributes.add(entry.getValue());
            }
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return ALL_CONFIG_ATTRIBUTE_MAP.values();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public void run(String... args) throws Exception {
        //服务启动后自动加载生效中的系统接口资源
        this.reloadAllInterfaceRes();
    }

    /**
     * 全量加载系统资源。系统资源有变更时，需要调用此方法更新接口访问规则
     */
    public void reloadAllInterfaceRes() {
        log.info("全量加载加载系统接口资源--开始");
        List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                .resType(SysResourceTypeEnum.INTERFACE.getResType())
                //只加载生效中的资源
                .status(SysResourceStatusEnum.NORMAL.getStatus())
                .build());
        ALL_CONFIG_ATTRIBUTE_MAP.clear();
        for (SysResourcePo sysResourcePo : sysResourcePos) {
            String resKey = sysResourcePo.getResReqMethod() + MsSecurityConstant.RES_REQ_METHOD_URL_SEPARATOR + sysResourcePo.getResUrl();
            ConfigAttribute configAttribute = new SecurityConfig(resKey);
            ALL_CONFIG_ATTRIBUTE_MAP.put(resKey, configAttribute);
        }
        log.info("全量加载加载系统接口资源--结束，共计加载接口{}个", ALL_CONFIG_ATTRIBUTE_MAP.size());
    }

}
