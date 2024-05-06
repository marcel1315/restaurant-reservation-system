package com.zerobase.shopreservation.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(
        info = @Info(
                title = "매장 테이블 예약 서비스",
                version = "1.0",
                description = "상점 예약을 할 수 있는 백엔드 API 입니다."
        ),
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(name = "BearerAuth", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("Members")
                .pathsToMatch("/member/**")
                .build();
    }

    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("Customer")
                .pathsToMatch("/customer/**")
                .build();
    }

    @Bean
    public GroupedOpenApi managerApi() {
        return GroupedOpenApi.builder()
                .group("Manager")
                .pathsToMatch("/manager/**")
                .build();
    }
}

