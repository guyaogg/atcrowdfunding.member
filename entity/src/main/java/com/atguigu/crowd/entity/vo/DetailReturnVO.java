package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author guyao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *     回报信息的主键
      */
    private Integer returnId;
    /**
     * 支持金额
     */
    private Integer supportMoney;
    /*
     * 单笔限购
     */
    private Integer signalPurchase;
    /**
     * 当前挡位支持者
     */
    private Integer supportCount;
    /**
     * 运费，“0”为包邮
     */
    private Integer freight;
    /**
     * 众筹结束后返还回报物品天数
     */
    private Integer returnDate;
    /**
     * 回报内容
     */
    private String content;

}
