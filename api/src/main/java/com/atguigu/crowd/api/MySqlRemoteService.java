package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author guyao
 */
@FeignClient("crowd-mysql")
public interface MySqlRemoteService {

    /**
     * 使用用户账户查询用户
     *
     * @param loginAcct 用户账户
     * @return 用户
     */
    @RequestMapping("/get/memberpo/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct);

    /**
     * 保存注册对象
     *
     * @param memberPo 注册对像
     * @return 保存结果
     */
    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody MemberPO memberPo);

    /**
     * 保存项目对象
     *
     * @param projectVO 项目对象
     * @param memberId  用户id
     * @return
     */
    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId);

    /**
     * 查询首页展示项目
     *
     * @return
     */
    @RequestMapping("/get/portal/type/project/data/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    /**
     * 获取详细项目信息
     *
     * @param projectId 项目id
     * @return
     */
    @RequestMapping("/get/project/detail/remote")
    ResultEntity<DetailProjectVO> getProjectDetailRemote(@RequestParam("projectId") Integer projectId);

    /**
     * 获取项目订单
     *
     * @param projectId
     * @param returnId
     * @return
     */
    @RequestMapping("/get/order/project/vo/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId, @RequestParam("returnId") Integer returnId);

    /**
     * 获取地址列表
     *
     * @param memberId
     * @return
     */
    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);

    /**
     * 保存地址列表
     *
     * @param addressVO
     * @return
     */
    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);
}
