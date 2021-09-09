package com.atguigu.crowd.controller;

import com.atguigu.crowd.api.MySqlRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShorMessageProperties;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.atguigu.crowd.constant.CrowdConstant.*;

/**
 * @author guyao
 */
@Slf4j
@Controller
public class MemberController {
    @Autowired
    private ShorMessageProperties shorMessageProperties;
    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySqlRemoteService mySqlRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        // 清空登录用户
        session.invalidate();
        return REDIRECT.getStr() + "http://101.132.45.198/crowd/";
    }
    /**
     * 处理登录请求
     *
     * @param loginacct
     * @param model
     * @return
     */
    @RequestMapping("/auth/member/login")
    public String register(@RequestParam("loginacct") String loginacct,
                           @RequestParam("userpswd") String userpswd,
                           Model model,
                           HttpSession session) {
        // 根据账号查询用户
        ResultEntity<MemberPO> resultEntity = mySqlRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        String result = resultEntity.getResult();
        MemberPO memberPO = resultEntity.getData();
        // 失败查询
        if (ResultEntity.FAIL.equals(result) || memberPO == null) {
            model.addAttribute(ATTR_NAME_MESSAGE.getStr(), "还未注册!请注册..");
            return "member-login";
        }
        // 查看密码是否匹配
        String encodeGetPswd = memberPO.getUserpswd();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(userpswd, encodeGetPswd);
        // 密码不匹配
        if (!matches) {
            model.addAttribute(ATTR_NAME_MESSAGE.getStr(), "密码不正确");
            return "member-login";
        }
        // 正确存入session域中
        session.setAttribute(ATTR_NAME_LOGIN_MEMBER.getStr(), new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail()));
        // 重定向到首页

        // 如果是拦截请求走拦截请求
        if(session.getAttribute(ATTR_NAME_INTERCEPT.getStr()) != null){
            String interceptPath = (String) session.getAttribute(ATTR_NAME_INTERCEPT.getStr());
            // 删去拦截路径，防止二次登录跳转别的地方，且省空间
            session.removeAttribute(ATTR_NAME_INTERCEPT.getStr());
            return REDIRECT.getStr() + interceptPath;

        }
        // 重定向到zuul网关去查找，不加http://101.132.45.198回找自己微服务的东西

        return REDIRECT.getStr() +"http://101.132.45.198/crowd/auth/member/to/center/page";
//        return "member-center";

    }

    /**
     * 保存注册用户
     *
     * @param memberVo 注册用户
     * @return 保存结果
     */
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVo, Model model) {
        // 账号密码不能为null
        String rawPswd = memberVo.getUserpswd();
        if (memberVo.getLoginacct() == null || rawPswd == null) {
            model.addAttribute(ATTR_NAME_MESSAGE.getStr(), "登陆账号和密码不可用为空值");
            return "member-reg";
        }
        // 获取验证码,手机号参数
        String phoneNum = memberVo.getPhoneNum();

        // 拼Redis的key键
        String key = REDIS_CODE_PREFIX_ + phoneNum;
        // 查询
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKey(key);

        String result = resultEntity.getResult();
        // 查询失败
        if (ResultEntity.FAIL.equals(result)) {
            model.addAttribute(ATTR_NAME_MESSAGE.getStr(), resultEntity.getMessage());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        // 查询不到
        if (redisCode == null) {
            model.addAttribute(ATTR_NAME_MESSAGE.getStr(), MESSAGE_CODE_NOT_EXISTS.getStr());
            return "member-reg";
        }
        String formCode = memberVo.getCode();
        // 查询不匹配
        if (!Objects.equals(redisCode, formCode)) {
            model.addAttribute(ATTR_NAME_MESSAGE.getStr(), MESSAGE_CODE_INVALID.getStr());
            return "member-reg";
        }
        // 密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String userpswd = memberVo.getUserpswd();
        String userpswdEncode = bCryptPasswordEncoder.encode(userpswd);
        memberVo.setUserpswd(userpswdEncode);
        // 保存
        // 常见空的po对象
        MemberPO memberPO = new MemberPO();
        // 赋值属性
        BeanUtils.copyProperties(memberVo, memberPO);
        // 调用远程方法
        ResultEntity<String> saveResultEntity = mySqlRemoteService.saveMember(memberPO);
        String saveResult = saveResultEntity.getResult();
        // 保存失败
        if (ResultEntity.FAIL.equals(saveResult)) {
            model.addAttribute(ATTR_NAME_MESSAGE.getStr(), saveResultEntity.getMessage());
            return "member-reg";
        }
        // 成功后删除redis的验证码
        redisRemoteService.removeRedisKeyRemote(key);

        return REDIRECT.getStr() + "http://101.132.45.198/crowd/auth/member/login?loginacct=" + memberVo.getLoginacct() + "&userpswd=" + rawPswd;

    }

    /**
     * 给手机号发送验证码短信
     *
     * @param phoneNum 手机号
     * @return 发送结果
     */
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
        // 判断是否发送过验证码
        String key = REDIS_CODE_PREFIX_ + phoneNum;
        ResultEntity<Long> resultEntity = redisRemoteService.getRedisKeyExpireRemote(key);
        String result = resultEntity.getResult();
        if (ResultEntity.FAIL.equals(result)) {
            return ResultEntity.fail(MESSAGE_SEVER_FAULT.getStr());
        }
        Long expire = resultEntity.getData();
        if (expire != -2) {
            return ResultEntity.fail("验证码还有" + (expire / 60) + "分钟" + (expire % 60) + "秒过期，请过期后再发送");
        }
        log.info(phoneNum);

        // 发送验证码到手机
        ResultEntity<String> sendResultEntity = CrowdUtil.sendShortMessage(phoneNum,
                shorMessageProperties.getAppcode(),
                shorMessageProperties.getSkin(),
                shorMessageProperties.getHost(),
                shorMessageProperties.getPath(),
                shorMessageProperties.getExpireAt());
        // 判断发送结果
        if (ResultEntity.SUCCESS.equals(sendResultEntity.getResult())) {
            // 发送成功存入redis
            // 返回随机生成的验证码
            String code = sendResultEntity.getData();
            log.info(code);
            // 调用远程接口存入redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, Long.parseLong(shorMessageProperties.getExpireAt()), TimeUnit.MINUTES);
            log.info(saveCodeResultEntity.getResult());
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }

        } else {
            return sendResultEntity;
        }

        // 失败返回失败


    }

    /**
     * 查询账号是否可用
     *
     * @param loginAcct 账号
     * @return 是否可用结果
     */
    @ResponseBody
    @RequestMapping("/auth/query/member/loginAcct/avail.json")
    public ResultEntity<String> queryLoginAcctAvail(@RequestParam("loginAcct") String loginAcct) {
        // 账号参数loginAcct
        // 使用mysql微服务查询用户
        ResultEntity<MemberPO> memberPoByLoginAcct = mySqlRemoteService.getMemberPOByLoginAcctRemote(loginAcct);
        String result = memberPoByLoginAcct.getResult();
        // 如果成功
        if (ResultEntity.FAIL.equals(result)) {
            // 返回可用
            return ResultEntity.successWithoutData();
        } else {
            return ResultEntity.fail(MESSAGE_LOGIN_ACCT_ALREADY_IN_USE.getStr());
        }


    }
}
