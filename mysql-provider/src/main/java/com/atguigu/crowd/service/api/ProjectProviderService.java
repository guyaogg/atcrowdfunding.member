package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;

import java.util.List;

/**
 * @author guyao
 */
public interface ProjectProviderService {
    /**
     * 保存项目
     * @param projectVO
     * @param memberId
     */
    void saveProject(ProjectVO projectVO, Integer memberId);

    /**
     * 查询首页展示项目
     * @return
     */
    List<PortalTypeVO> getPortalTypeVO();

    /**
     * 查询详细项目对象
     * @param projectId 项目id
     * @return
     */
    DetailProjectVO getDetailProjectVO(Integer projectId);

}
