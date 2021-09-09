package com.atguigu.crowd.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 发送短信的可变参数
 * @author guyao
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "short.message")
public class ShorMessageProperties {
    private String appcode;
    private String skin;
    private String host;
    private String path;
    private String expireAt;
}
