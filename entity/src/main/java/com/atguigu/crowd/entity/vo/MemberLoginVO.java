package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录的用户类
 * @author guyao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVO implements Serializable {
    private static final long serialVersionUID = -71021L;
    private Integer id;

    private String username;

    private String email;

}
