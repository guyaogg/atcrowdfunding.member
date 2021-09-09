package com.atguigu.crowd.handler;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.service.api.ProjectProviderService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author guyao
 */
@RestController
public class ProjectProviderHandler {
    @Autowired
    private ProjectProviderService projectProviderService;

    @RequestMapping("/get/project/detail/remote")
    public ResultEntity<DetailProjectVO> getProjectDetailRemote(@RequestParam("projectId")Integer projectId) {
        try {
            DetailProjectVO detailProjectVO = projectProviderService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }

    }
    @RequestMapping("/save/project/vo/remote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){
        // 调用本地Service执行保存
        try {
            projectProviderService.saveProject(projectVO,memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
           return ResultEntity.fail(e.getMessage());
        }

    }
    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote(){
        try {
            List<PortalTypeVO> portalTypeVOList = projectProviderService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }
}
