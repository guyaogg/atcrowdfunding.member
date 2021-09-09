package com.atguigu.crowd.controller;

import com.atguigu.crowd.api.MySqlRemoteService;
import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guyao
 */
@Slf4j
@Controller
public class ProjectConsumerController {
    private static final long HEADER_MAXSIZE = 1024 * 1024 * 5;
    private static final long DETAIL_MAXSIZE = 1024 * 1024 * 10;

    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private MySqlRemoteService MySqlRemoteService;

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId")Integer projectId,Model model){
        ResultEntity<DetailProjectVO> resultEntity = MySqlRemoteService.getProjectDetailRemote(projectId);
        String result = resultEntity.getResult();
        if(ResultEntity.SUCCESS.equals(result)){
            DetailProjectVO detailProjectVO = resultEntity.getData();
            model.addAttribute("detailProjectVO", detailProjectVO);
        }
        return "project-detail";

    }
    @RequestMapping("/create/confirm")
    public String createConfirm(HttpSession session,
                                MemberConfirmInfoVO memberConfirmInfoVO,
                                Model model) {

        // 从session域中读取projectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT.getStr());
        if (projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPORARY_PROJECT_MISSING.getStr());
        }
        // 将确认信息设置到projectVO对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 从session域中读取memberVO对象
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER.getStr());
        Integer memberId = memberLoginVO.getId();
        // 调用远程方法持久化projectVO对象
        ResultEntity<String> saveResultEntity = MySqlRemoteService.saveProjectVORemote(projectVO, memberId);
        // 判断是否保存成功
        String result = saveResultEntity.getResult();
        if (ResultEntity.FAIL.equals(result)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), saveResultEntity.getMessage());
            return "project-confirm";
        }
        // 将临时ProjectVO移除session
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT.getStr());
        return CrowdConstant.REDIRECT.getStr() + "http://101.132.45.198/project/create/success/page";

    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> createSaveReturn(ReturnVO returnVO,
                                                 HttpSession session) {
        List<ReturnVO> returnVOList = null;
        try {
            // 从session中拿到之前的projectVO
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT.getStr());
            // 判断是否为空
            if (projectVO == null) {
                return ResultEntity.fail(CrowdConstant.MESSAGE_TEMPORARY_PROJECT_MISSING.getStr());
            }

            // 从projectVO中获取returnVOList集合
            returnVOList = projectVO.getReturnVOList();

            // returnVOList集合是否有效
            if (returnVOList == null) {
                returnVOList = new ArrayList<>();
                // 将新建的集合放入projectVO对象中
                projectVO.setReturnVOList(returnVOList);
            }
            returnVOList.add(returnVO);
            // 更新session中的projectVO
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT.getStr(), projectVO);
            // 所有操作成功
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }

    }

    /**
     * @param returnPicture 请求参数名字
     * @return 上传结果
     */
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(
            // 接收用户上传的文件
            @RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        if (returnPicture.getSize() > HEADER_MAXSIZE) {
            return ResultEntity.fail(CrowdConstant.MESSAGE_PICTURE_IS_TOO_BIG.getStr());
        }
        ResultEntity<String> uploadReturnPictureResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename()
        );
        // 返回判断上传结果
        return uploadReturnPictureResultEntity;

    }

    @RequestMapping("/create/project/information")
    public String saveProjectTableInfo(
            // 接收除图片的信息
            ProjectVO projectVO,
            // 接收头图
            MultipartFile headerPicture,
            // 详情图片
            List<MultipartFile> detailPictures,
            // 将部分 projectVO存入session域
            HttpSession session,
            // 将错误信息存入返回的模型中
            Model model) throws IOException {

        // 头图不为空
        if (headerPicture == null || headerPicture.isEmpty()) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), CrowdConstant.MESSAGE_HEADER_IS_EMPTY.getStr());
            // 上传失败返回原来界面
            return "member-start-step";
        }
        // 完成头图上传

        log.warn("头图大小为" + headerPicture.getSize());
        if (headerPicture.getSize() > HEADER_MAXSIZE) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), CrowdConstant.MESSAGE_HEADER_IS_TOO_BIG.getStr());

            return "member-start-step";
        }
        // 如果用户上传了有内容的文件上传
        ResultEntity<String> uploadFileToOssResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename()
        );
        String result = uploadFileToOssResultEntity.getResult();
        // 判断头图是否上传成功
        if (ResultEntity.SUCCESS.equals(result)) {
            // 从返回中拿到图片访问路径
            String headerPicturePath = uploadFileToOssResultEntity.getData();
            // 存入项目模型中
            projectVO.setHeaderPicturePath(headerPicturePath);


        } else {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), CrowdConstant.MESSAGE_HEADER_UPLOAD_FAULT.getStr());

            // 上传失败返回原来界面
            return "member-start-step";
        }
        // 创建存放详情图片路径的集合
        List<String> detailPicturePathList = new ArrayList<>();
        if (detailPictures == null || detailPictures.size() == 0) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY.getStr());

            // 上传失败返回原来界面
            return "member-start-step";
        }
        // 遍历detailPictures集合
        long detailPictureSize = 0;
        for (MultipartFile detailPicture : detailPictures) {
            detailPictureSize += detailPicture.getSize();
        }
        log.warn("详情图大小为" + detailPictureSize);
        if (detailPictureSize > DETAIL_MAXSIZE) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), CrowdConstant.MESSAGE_DETAIL_IS_TOO_BIG.getStr());
            return "member-start-step";
        }
        for (MultipartFile detailPicture : detailPictures) {
            if (!detailPicture.isEmpty()) {
                // 执行上传
                ResultEntity<String> detailUpLoadResultEntity = CrowdUtil.uploadFileToOss(
                        ossProperties.getEndPoint(),
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        detailPicture.getInputStream(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        detailPicture.getOriginalFilename()
                );
                String detailUpLoadResult = detailUpLoadResultEntity.getResult();
                // 这里就不验证错误了，太多错误用户体验不好
                if (ResultEntity.SUCCESS.equals(detailUpLoadResult)) {
                    String detailPicturePath = detailUpLoadResultEntity.getData();
                    // 收集上传图片的访问路径
                    detailPicturePathList.add(detailPicturePath);
                }
            }
        }
        // 将详情访问路径集合存到projectVO里
        projectVO.setDetailPicturePathList(detailPicturePathList);
        // 将projectVO存到session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT.getStr(), projectVO);
        // 重定向到回报信息界面
        return CrowdConstant.REDIRECT.getStr() + "http://101.132.45.198/project/return/info/page";
    }

}
