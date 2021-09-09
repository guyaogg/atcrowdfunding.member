package com.atguigu.crowd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @EnableZuulProxy 开启网关功能
 * @author guyao
 */
@EnableZuulProxy
@SpringBootApplication
public class ZuulMainClass {
    public static void main(String[] args) {
        SpringApplication.run(ZuulMainClass.class,args);
    }
}
