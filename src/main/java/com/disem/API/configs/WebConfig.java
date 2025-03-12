
package com.disem.API.configs;

import com.disem.API.component.RoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RoleInterceptor roleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/webservice/usuarios", "/api/webservice/remover-usuario", "/api/webservice/atualizar-usuario");

        registry.addInterceptor(roleInterceptor)
                .addPathPatterns(
                        "/api/webservice/usuarios",
                        "/api/webservice/remover-usuario",
                        "/api/webservice/atualizar-usuario"
                )
                .order(1);
    }
}

