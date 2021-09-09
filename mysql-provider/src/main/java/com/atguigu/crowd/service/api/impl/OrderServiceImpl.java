package com.atguigu.crowd.service.api.impl;

import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.po.AddressPOExample;
import com.atguigu.crowd.entity.po.OrderPO;
import com.atguigu.crowd.entity.po.OrderProjectPO;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.mapper.AddressPOMapper;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.mapper.OrderProjectPOMapper;
import com.atguigu.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guyao
 */
@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {
    @SuppressWarnings("all")
    @Autowired
    private OrderPOMapper orderPOMapper;
    @SuppressWarnings("all")
    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;
    @SuppressWarnings("all")
    @Autowired
    private AddressPOMapper addressPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVORemote(Integer projectId, Integer returnId) {

        return orderProjectPOMapper.selectOrderProjectVO(projectId, returnId);
    }


    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {
        AddressPOExample addressPOExample = new AddressPOExample();
        AddressPOExample.Criteria criteria = addressPOExample.createCriteria();
        criteria.andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(addressPOExample);
        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,readOnly = false)
    @Override
    public void saveAddress(AddressVO addressVO) {
        // 增删改记得加上事务
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,readOnly = false)
    @Override
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);
        // 保存时返回id
        orderPOMapper.insert(orderPO);
        Integer id = orderPO.getId();
        orderProjectPO.setOrderId(id);
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
