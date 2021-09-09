package com.atguigu.crowd.controller;

import com.atguigu.crowd.api.MySqlRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @author guyao
 */
@Controller
public class PortalController {
    @Autowired
    private MySqlRemoteService mySqlRemoteService;


    @RequestMapping("/")
    public String showPortalPage(Model model) {
        // 调用mySqlRemoteService查询首页数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySqlRemoteService.getPortalTypeProjectDataRemote();
        // 检查查询结果
        String result = resultEntity.getResult();
        if(ResultEntity.SUCCESS.equals(result)){
            // 取出数据到模型
            List<PortalTypeVO> data = resultEntity.getData();
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA.getStr(), data);
        }

        // 少了加载数据
        return "portal";
    }
}
