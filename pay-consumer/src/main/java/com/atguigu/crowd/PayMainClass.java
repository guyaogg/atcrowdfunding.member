package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author guyao
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PayMainClass {
    public static void main(String[] args) {
        SpringApplication.run(PayMainClass.class,args);
    }
}