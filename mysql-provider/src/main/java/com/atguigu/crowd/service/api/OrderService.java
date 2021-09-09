package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;

import java.util.List;

/**
 * @author guyao
 */
public interface OrderService {
    OrderProjectVO getOrderProjectVORemote(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
