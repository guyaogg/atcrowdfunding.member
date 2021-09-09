package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.mapper.MemberMapper;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import com.atguigu.crowd.service.api.MemberService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author guyao
 */
@SpringBootTest
public class MyBatisTest {
    Logger logger = LoggerFactory.getLogger(MyBatisTest.class);
    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private ProjectPOMapper projectPOMapper;
    @Test
    public void MapperTest()  {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String source = "123456";
        String encode = bCryptPasswordEncoder.encode(source);
        MemberPO memberPO = new MemberPO(null, "jack", encode, "杰克", "jack@qq.com", 1, 1, "杰克", "123123", 2);
        int insert = memberMapper.insert(memberPO);
        logger.debug(String.valueOf(insert));
    }
    @Test
    public void test() throws SQLException {
        MemberPO jack = memberService.getMemberPOByLoginAcct("cc");
        logger.info(String.valueOf(jack));
    }
    @Test
    public void testProjectPOMapper()  {
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(13);
        logger.info(String.valueOf(detailProjectVO));
    }
}
