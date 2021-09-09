package com.atguigu.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author guyao
 */
@MapperScan("com.atguigu.crowd.mapper")
@SpringBootApplication
public class MysqlMainClass {
    public static void main(String[] args) {
        SpringApplication.run(MysqlMainClass.class,args);
    }
}

