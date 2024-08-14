package com.flybearblog.blogserve.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j配置类
 *
 * @author java@tedu.cn
 * @version 3.0
 */
@Slf4j
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    /**
     * 【重要】指定Controller包路径
     */
    private static final String BASE_PACKAGE = "cn.tedu.tmall";
    /**
     * 组名
     */
    private static final String GROUP_NAME = "学茶商城";
    /**
     * 主机名
     */
    private static final String HOST = "http://java.tedu.cn";
    /**
     * 标题
     */
    private static final String TITLE = "学茶商城-单点登录服务-在线API文档";
    /**
     * 简介
     */
    private static final String DESCRIPTION = "学茶商城-单点登录服务-在线API文档";
    /**
     * 服务条款URL
     */
    private static final String TERMS_OF_SERVICE_URL = "http://www.apache.org/licenses/LICENSE-2.0";
    /**
     * 联系人
     */
    private static final String CONTACT_NAME = "Java教学研发部";
    /**
     * 联系网址
     */
    private static final String CONTACT_URL = "http://java.tedu.cn";
    /**
     * 联系邮箱
     */
    private static final String CONTACT_EMAIL = "java@tedu.cn";
    /**
     * 版本号
     */
    private static final String VERSION = "2.0";

    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

    public Knife4jConfiguration() {
        log.debug("创建配置类对象：Knife4jConfiguration");
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(HOST)
                .apiInfo(apiInfo())
                .groupName(GROUP_NAME)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(GROUP_NAME));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .termsOfServiceUrl(TERMS_OF_SERVICE_URL)
                .contact(new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL))
                .version(VERSION)
                .build();
    }

}
