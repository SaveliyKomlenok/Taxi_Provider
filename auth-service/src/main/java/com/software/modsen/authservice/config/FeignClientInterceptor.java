package com.software.modsen.authservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static com.software.modsen.authservice.util.AuthParameters.AUTH_HEADER;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    public static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader(AUTH_HEADER);
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(AUTH_HEADER, getBearerTokenHeader());
    }
}
