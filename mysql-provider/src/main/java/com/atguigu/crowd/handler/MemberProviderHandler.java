package com.atguigu.crowd.handler;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.service.api.MemberService;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guyao
 */
@Slf4j
@RestController
public class MemberProviderHandler {
    @Autowired
    private MemberService memberService;
    @RequestMapping("/get/memberpo/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct")String loginAcct){

        MemberPO memberPO = null;
        try {
            // 调用本地service完成查询
            memberPO = memberService.getMemberPOByLoginAcct(loginAcct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            // 失败返回失败信息
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }

    }

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPo) {
        try {
            log.info(String.valueOf(memberPo));
           int save =  memberService.saveMember(memberPo);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                return ResultEntity.fail(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE.getStr());
            }
            return ResultEntity.fail(e.getMessage());

        }
    }
}
