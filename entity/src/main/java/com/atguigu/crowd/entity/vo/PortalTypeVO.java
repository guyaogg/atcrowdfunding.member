package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author guyao
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortalTypeVO implements Serializable {
    private static final long serialVersionUID = -710L;
    private Integer id;

    private String name;

    private String remark;
    private List<PortalProjectVO> portalProjectVOList;
}
