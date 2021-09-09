package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.po.MemberPO;

/**
 * @author guyao
 */
public interface MemberService {
    /**
     * 查询账号对应的用户
     *
     * @param loginAcct 账号
     * @return 查询用户结果
     */
    MemberPO getMemberPOByLoginAcct(String loginAcct);

    /**
     * 保存注册对象
     *
     * @param memberPo 注册用户
     * @return 影响行数
     */
    int saveMember(MemberPO memberPo);
}
