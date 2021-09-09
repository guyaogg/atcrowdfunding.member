package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author guyao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer projectId;
    private String projectName;
    private String projectDesc;
    private Integer follower;
    private Integer status;
    private Integer day;
    private String statusText;
    private Integer money;
    private Integer supportMoney;
    private Integer percentage;
    private String deployDate;
    private Integer lastDay;
    private Integer supportCount;
    private String headerPicturePath;
    private List<String> detailPicturePathList;
    private List<DetailReturnVO> detailReturnVOList;
}
