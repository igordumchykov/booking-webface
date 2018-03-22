package com.jdum.booking.webface;

import com.jdum.booking.webface.config.AppConfig;
import com.jdum.booking.webface.config.SwaggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

//@EnableGlobalMethodSecurity
@SpringBootApplication(scanBasePackages = {"com.jdum.booking"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@Import({AppConfig.class, SwaggerConfig.class})
public class Webface {

    public static void main(String[] args) {
        SpringApplication.run(Webface.class, args);
    }

}