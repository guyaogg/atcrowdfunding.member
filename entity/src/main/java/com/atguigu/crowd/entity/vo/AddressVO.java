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
public class AddressVO implements Serializable {
    private static final long serialVersionUID = -710L;

    private Integer id;

    private String receiveName;

    private String phoneNum;

    private String address;

    private Integer memberId;
}
