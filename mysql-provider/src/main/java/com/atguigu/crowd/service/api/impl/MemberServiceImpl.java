package com.atguigu.crowd.service.api.impl;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.mapper.MemberMapper;
import com.atguigu.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author guyao
 * @Transactional(readOnly = true) 声明式事务（只读，操作查询
 */
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginAcct) {
        // 创建Example对象
        MemberPOExample memberPOExample = new MemberPOExample();
        // 创建criteria对象
        MemberPOExample.Criteria criteria = memberPOExample.createCriteria();
        // 封装查询条件
        criteria.andLoginacctEqualTo(loginAcct);
        // 执行查询
        List<MemberPO> memberPOS = memberMapper.selectByExample(memberPOExample);
        // 返回结果
        return memberPOS.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,readOnly = false)
    @Override
    public int saveMember(MemberPO memberPo) {
        int insert = memberMapper.insertSelective(memberPo);
        return insert;
    }
}
