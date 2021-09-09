package com.atguigu.crowd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心
 * @author guyao
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaMainClass {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMainClass.class,args);
    }
}