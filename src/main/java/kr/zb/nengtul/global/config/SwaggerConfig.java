package kr.zb.nengtul.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("Nengtul API docs")
        .description("제로베이스 백엔드 부트캠프 10기 팀 프로젝트 1조")
        .version("v1.0.0");

    String jwt = "JWT";
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
    Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
        .name(jwt)
        .type(SecurityScheme.Type.HTTP)
        .scheme("Bearer")
        .bearerFormat("Access Token"));

    return new OpenAPI()
        .info(info)
        .addSecurityItem(securityRequirement)
        .components(components);
  }
}