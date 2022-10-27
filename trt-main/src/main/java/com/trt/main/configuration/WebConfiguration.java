package com.trt.main.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * PROJECT: api-service
 * PACKAGE: com.jd.mlaas.apiservice.configuration
 * DESC: Please input descriptions...
 *
 * @author huzhanfei
 * @since 2018/10/16
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
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
//        registry.addResourceHandler("/null/swagger-resources/**")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/swagger-ui.html/swagger-resources/**")
//                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/swagger-resources/**")
                .addResourceLocations("classpath:/META-INF/resources/");

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(
                "/null/api-docs",
                "/api-docs").setKeepQueryParams(true);
        registry.addRedirectViewController(
                "/null/swagger-resources/configuration/ui",
                "/swagger-resources/configuration/ui");
        registry.addRedirectViewController(
                "/null/swagger-resources/configuration/security",
                "/swagger-resources/configuration/security");
        registry.addRedirectViewController(
                "/null/swagger-resources",
                "/swagger-resources");

        registry.addRedirectViewController(
                "/swagger-ui.html/api-docs",
                "/api-docs").setKeepQueryParams(true);
        registry.addRedirectViewController(
                "/swagger-ui.html/swagger-resources/configuration/ui",
                "/swagger-resources/configuration/ui");
        registry.addRedirectViewController(
                "/swagger-ui.html/swagger-resources/configuration/security",
                "/swagger-resources/configuration/security");
        registry.addRedirectViewController(
                "/swagger-ui.html/swagger-resources",
                "/swagger-resources");
    }
}
