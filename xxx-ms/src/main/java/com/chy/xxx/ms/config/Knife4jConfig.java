package com.chy.xxx.ms.config;

import com.chy.xxx.ms.properties.JwtTokenProperties;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Knife4j接口文档配置
 *
 * @author chy
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Resource
    private JwtTokenProperties jwtTokenProperties;

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                //api接口扫描规则：扫描 @Api 标注的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
        // 请求头必须填token
        // 通过页面登录后，token会存储到cookie中，请求时自动携带，在浏览器接口文档中调试时可以不加这个配置
        docket.securitySchemes(this.securitySchemes()).securityContexts(this.securityContexts());
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("chy", "https://www.chy.com/xxx-ms", "chy@qq.com"))
                .title("xxx-ms接口文档")
                .description("xxx管理后台接口文档")
                .version("1.0")
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        //设置请求头信息
        List<SecurityScheme> result = new ArrayList<>();
        String requestHeader = jwtTokenProperties.getRequestHeader();
        ApiKey apiKey = new ApiKey(requestHeader, requestHeader, "header");
        result.add(apiKey);
        return result;
    }

    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(this.getContextByPath("/*/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(this.defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference(jwtTokenProperties.getRequestHeader(), authorizationScopes));
        return result;
    }

}