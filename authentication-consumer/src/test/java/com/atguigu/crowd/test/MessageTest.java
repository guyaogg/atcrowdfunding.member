package com.atguigu.crowd.test;

import com.atguigu.crowd.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试第三方短信接口
 *
 * @author guyao
 */
@SpringBootTest
public class MessageTest {
    private Logger logger =  LoggerFactory.getLogger(MessageTest.class);
    @Test
    public void test() {

        String host = "https://dfsns.market.alicloudapi.com";

        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "ce553176a873421889b746b77040884a";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        // 验证码，时效（短信格式
        bodys.put("content", "code:1313,expire_at:5");
        // 手机号
        bodys.put("phone_number", "1816807175656132");
        // 样式
        bodys.put("template_id", "TPL_0001");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            StatusLine statusLine = response.getStatusLine();
            // 状态码
            int statusCode = statusLine.getStatusCode();
            String reasonPhrase = statusLine.getReasonPhrase();
            logger.info(String.valueOf(reasonPhrase));// ok
            logger.info(String.valueOf(statusCode));// 200
            logger.info(String.valueOf(response));// HTTP/1.1 200 OK [Date: Tue, 10 Aug 2021 11:38:00 GMT, Content-Type: applicatio...
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
