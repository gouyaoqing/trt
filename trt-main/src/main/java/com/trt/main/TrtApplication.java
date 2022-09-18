package com.trt.main;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.trt")
@MapperScan(basePackages = "com.trt.common.data.mapper")
@Slf4j
public class TrtApplication {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext context = SpringApplication.run(TrtApplication.class, args);
            if (context.isRunning()) {
                Thread.currentThread().join();
            }
        } catch (Exception e) {
            log.error("application start error. primarySource={} args={}", new Object[]{"TrtApplication", args, e});
            System.exit(1);
        }
    }

}
