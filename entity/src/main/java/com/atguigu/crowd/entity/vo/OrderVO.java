package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author guyao
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderVO implements Serializable {
    private static final long serialVersionUID = -710L;
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 支付宝流水号
     */
    private String payOrderNum;
    /**
     * 订单金额
     */
    private Double orderAmount;
    /**
     * 发票需求
     */
    private Integer invoice;

    /**
     * 发票抬头
     */
    private String invoiceTitle;
    /**
     * 订单备注
     */
    private String orderRemark;
    /**
     * 地址id
     */
    private Integer addressId;
    /**
     * 订单项目模型
     */
    private OrderProjectVO orderProjectVO;
}
