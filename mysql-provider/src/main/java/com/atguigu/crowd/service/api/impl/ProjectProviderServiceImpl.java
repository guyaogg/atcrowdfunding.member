package com.atguigu.crowd.service.api.impl;

import com.atguigu.crowd.entity.po.MemberConfirmInfoPO;
import com.atguigu.crowd.entity.po.MemberLaunchInfoPO;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.service.api.ProjectProviderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author guyao
 */
@Transactional(readOnly = true)
@Service
public class ProjectProviderServiceImpl implements ProjectProviderService {
    @Autowired
    private ReturnPOMapper returnPOMapper;
    @Autowired
    private ProjectPOMapper projectPOMapper;
    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;
    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {
        if(projectVO == null || memberId == null){
            throw new RuntimeException("信息已过期，从新填写");
        }
        // 保存ProjectPO对象
        // 创建ProjectPO空对象
        ProjectPO projectPO = new ProjectPO();
        // 保存相同属性
        BeanUtils.copyProperties(projectVO, projectPO);
        // 修复bug memberId修复到projectVO对象中
        projectPO.setMemberid(memberId);
        // 生成创建时间
        String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createDate);
        // 设置状态(0,即将开始
        projectPO.setStatus(0);
        // 保存projectPO
        // 为了能够获取到projectPO保存后自增的主键，需要到xml中进行相关设置
        projectPOMapper.insertSelective(projectPO);
        // 获取自增主键
        Integer projectId = projectPO.getId();

        // 保存项目、分类的关联关系信息
        // 获取相关信息
        List<Integer> typeIdList = projectVO.getTypeIdList();
        if (typeIdList != null) {
            projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        }
        // 保存项目、标签的关联信息
        List<Integer> tagIdList = projectVO.getTagIdList();
        if (tagIdList != null) {
            projectPOMapper.insertTagRelationship(tagIdList, projectId);

        }
        // 保存项目中详情图片路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        if(detailPicturePathList != null){
            projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);

        }

        // 项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        if (memberLauchInfoVO != null) {
            MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
            BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
            memberLaunchInfoPO.setMemberid(memberId);
            memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);
        }

        // 保存回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();

        if (returnVOList != null) {
            List<ReturnPO> returnPOList = new ArrayList<>();
            for (ReturnVO returnVO : returnVOList) {
                ReturnPO returnPO = new ReturnPO();
                BeanUtils.copyProperties(returnVO, returnPO);
                returnPOList.add(returnPO);

            }
            returnPOMapper.insertReturnPOBath(returnPOList, projectId);
        }



        // 确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        if (memberConfirmInfoVO != null) {
            MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
            BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
            memberConfirmInfoPO.setMemberid(memberId);
            memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
        }


    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId){
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
        Integer status = detailProjectVO.getStatus();
        // 确定状态
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }

        // 根据失效时间计算日期
        String deployDate = detailProjectVO.getDeployDate();
        // 获取当前日期
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date deployDay = null;
        try {
            deployDay = format.parse(deployDate);
            // 获取当前日期时间戳
            long currentTimeStamp = date.getTime();
            // 获取众筹时间
            long deployDayTimeStamp = deployDay.getTime();

            // 两时间相减得到过去的时间天数
            long pastDays =  (currentTimeStamp - deployDayTimeStamp) / 1000/60/60/24;
            // 使用总天数- 众筹天数
            Integer totalDays = detailProjectVO.getDay();
            Integer lastDay = (int)(totalDays - pastDays);
            detailProjectVO.setLastDay(lastDay);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        //        返回处理好的值
        return detailProjectVO;

    }
}
