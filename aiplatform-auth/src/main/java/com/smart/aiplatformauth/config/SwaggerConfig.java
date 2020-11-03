package com.smart.aiplatformauth.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @desc: 配置Swagger调试和文档生成
 * @author: chengjz
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket applicationApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("aiplatform")
                .select()  // 选择哪些路径和api会生成document
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()) // 对所有路径进行监控
                .build()
                .apiInfo(applicationInfo());//用来创建该Api的基本信息(这些基本信息会展现在文档页面中)
    }

    private ApiInfo applicationInfo() {
        return new ApiInfoBuilder()
            .title("AIplatform-Auth")
            .description("SpingCloud-授权微服务API-用于登录、权限管理、BPM工作流引擎服务 "
                + "正式系统通过网关访问请前面加【http://aq.tlhub.cn:9000/aiplatformauth/】   "
                + "不通过网关请求仅测试使用请前面加【http://aq.tlhub.cn:9010/】")
            .version("1.0.0")
            .build();
    }

}
