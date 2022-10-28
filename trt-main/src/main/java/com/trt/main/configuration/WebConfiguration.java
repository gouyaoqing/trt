package com.trt.main.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePathPatterns = {
                "/swagger-ui.html/**", "/error", "/webjars/**", "/swagger-resources/**", "/web/v1/check-user-status", "/web/v1/api/**", "/api/v1/**", "/ext/**", "/web/v1/xbp/**",
                "/queryIpAddress", "/subsystem/**", "/index.html", "/operateMonitorData", "/machineAliveStatus",
                "/queryMonitorData", "/*/alarm/settings", "/alarms/statistics", "/alarm/records",
        };
//        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludePathPatterns);
//        registry.addInterceptor(manageInterceptor()).addPathPatterns("/web/v1/manage/**").excludePathPatterns("/swagger-ui.html/**", "/error", "/webjars/**", "/swagger-resources/**", "/web/v1/api/**", "/web/v1/xbp/**", "/ext/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/swagger-resources/**")
//                .addResourceLocations("classpath:/META-INF/resources/");
        super.addResourceHandlers(registry);
    }

}
