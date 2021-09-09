package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @EnableDiscoveryClient 开启客户端现版本可省
 * @author guyao
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ProjectMainClass {
    public static void main(String[] args) {
        SpringApplication.run(ProjectMainClass.class,args);
    }
}
