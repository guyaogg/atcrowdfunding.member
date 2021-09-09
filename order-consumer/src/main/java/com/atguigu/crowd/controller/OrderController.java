package com.atguigu.crowd.controller;

import com.atguigu.crowd.api.MySqlRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author guyao
 */
@Slf4j
@Controller
public class OrderController {
    @Autowired
    private MySqlRemoteService mySqlRemoteService;



    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO,
                              HttpSession session) {
        // 执行地址信息保存
        ResultEntity<String> resultEntity = mySqlRemoteService.saveAddressRemote(addressVO);
        log.info("地址保存处理结果" + resultEntity.getResult());
        // 从session域获取orderProjectVO对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        // 从对象获取returnCount
        Integer returnCount = orderProjectVO.getReturnCount();
        // 重定向回表单，将新地址刷新出来
        return CrowdConstant.REDIRECT.getStr() + "http://101.132.45.198/order/confirm/order/" + returnCount;

    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfor(@PathVariable("returnCount") Integer returnCount,
                                        HttpSession session,
                                        Model model) {
        // 接收回报数量合并到session中
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        if(orderProjectVO == null){
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), "长时间未操作，重新填写表单");
            return "confirm-return";
        }
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO", orderProjectVO);
        // 获取当前用户id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER.getStr());
        Integer memberId = memberLoginVO.getId();
        // 查询目前收货地址数据
        ResultEntity<List<AddressVO>> resultEntity = mySqlRemoteService.getAddressVORemote(memberId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> addressVOList = resultEntity.getData();
            session.setAttribute("addressVOList", addressVOList);
        }
        return "confirm-order";
    }

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("returnId") Integer returnId,
                                        // 存入会话中方便跳转使用
                                        HttpSession session) {

        ResultEntity<OrderProjectVO> resultEntity = mySqlRemoteService.getOrderProjectVORemote(projectId, returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            OrderProjectVO orderProjectVO = resultEntity.getData();
            session.setAttribute("orderProjectVO", orderProjectVO);
        }
        return "confirm-return";


    }
}
