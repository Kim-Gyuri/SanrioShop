package com.example.demoshop.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.example.demoshop.utils.constants.JwtConstants.JWT_AUTH;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .version("1.0")
                        .description("API documentation for my application")
                )
                .addSecurityItem(new SecurityRequirement().addList("custom-auth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("custom-auth", new SecurityScheme()
                                .type(Type.APIKEY)
                                .in(SecurityScheme.In.HEADER) // Header 위치
                                .name(JWT_AUTH) // 사용자 정의 헤더 이름
                        )
                );
    }


    /**
     *   application/octet-stream을 다뤄주는 HttpMessageConverter를 추가
     */
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        private OctetStreamReadMsgConverter octetStreamReadMsgConverter;

        @Autowired
        public WebConfig(OctetStreamReadMsgConverter octetStreamReadMsgConverter) {
            this.octetStreamReadMsgConverter = octetStreamReadMsgConverter;
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(octetStreamReadMsgConverter);
        }
    }


    @Component
    public class OctetStreamReadMsgConverter extends AbstractJackson2HttpMessageConverter {
        @Autowired
        public OctetStreamReadMsgConverter(ObjectMapper objectMapper) {
            super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
        }

        // 기존 application/octet-stream 타입을 쓰기로 다루는 메시지 컨버터가 이미 존재 (ByteArrayHttpMessageConverter)
        // 따라서 해당 컨버터는 쓰기 작업에는 이용하면 안됨
        @Override
        public boolean canWrite(Class<?> clazz, MediaType mediaType) {
            return false;
        }


        @Override
        protected boolean canWrite(MediaType mediaType) {
            return false;
        }
    }

}
