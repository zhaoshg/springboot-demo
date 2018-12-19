package com.hlgf.wx.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.hlgf.sms.model.WxBindEntity;
import com.hlgf.sms.service.Sender;
import com.hlgf.wx.exception.GlobalException;
import com.hlgf.wx.model.AccessToken;
import com.hlgf.wx.model.WxUserInfo;
import com.hlgf.wx.service.WxService;
import com.hlgf.wx.utils.JacksonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Controller
public class BindController {


    @Autowired
    private WxService wxService;

    @Resource(name = "CUCCSender")
    private Sender smsSender;

    @Autowired
    private DefaultKaptcha captchaProducer;


    @RequestMapping("/wx/showBind")
    public String showBind(HttpServletRequest request, HttpSession session, ModelMap model) throws GlobalException {
        WxUserInfo userInfo = null;
        if (session.getAttribute("currentUser") != null)
            userInfo = (WxUserInfo) session.getAttribute("currentUser");
        else {
            String code = request.getParameter("code");
            AccessToken token = wxService.getWebAccessToken(code);
            if (token == null || token.getAccess_token() == null)
                throw new GlobalException("获取token失败");

            userInfo = wxService.getUserInfo(token.getAccess_token(), token.getOpenid());
            if (userInfo == null)
                throw new GlobalException("获取UserInfo失败");
            session.setAttribute("currentUser", userInfo);
        }
        model.addAttribute("userinfo", userInfo);
        WxBindEntity oldBind = wxService.getBindInfo(userInfo.getOpenid());
        model.addAttribute("bindinfo", oldBind);
        String type = request.getParameter("type");
        if (oldBind != null && type == null) {
            return "bindInfo";
        } else
            return "bind";
    }


    @RequestMapping("/bindRes")
    public String showBind(HttpServletRequest request, ModelMap model) throws GlobalException {
        String res = request.getParameter("res");
        model.addAttribute("res", res);
        return "result";
    }


    @RequestMapping("/bindPhone")
    public @ResponseBody
    Map<String, Object> bind(HttpServletRequest request, HttpSession session, ModelMap model) throws GlobalException {
        Map<String, Object> bindRes = new HashMap<>();
        bindRes.put("res", Boolean.TRUE);

        String smsCode = request.getParameter("smsCode");
        String sessionCode = (String) session.getAttribute("bindCode");
        String openid = request.getParameter("openid");
        String nickname = request.getParameter("nickname");
        String phone = request.getParameter("phone");

        log.info("Session  bindCode {}  form smsCode {}", sessionCode, smsCode);

        if (null == smsCode || !smsCode.equals(sessionCode)) {
            bindRes.put("res", Boolean.FALSE);
            bindRes.put("msg", "错误的短信验证码");
            return bindRes;
        }
        if (!JacksonUtil.validateMobile(phone)) {
            bindRes.put("res", Boolean.FALSE);
            bindRes.put("msg", "手机号格式错误");
            return bindRes;
        }
        if (openid == null || nickname == null) {
            bindRes.put("res", Boolean.FALSE);
            bindRes.put("msg", "授权错误，请刷新页面");
            return bindRes;
        }

        WxBindEntity bindEntity = new WxBindEntity();
        bindEntity.setNickName(nickname);
        bindEntity.setOpenId(openid);
        bindEntity.setPhone(phone);
        boolean result = wxService.bindWX(bindEntity);
        bindRes.put("res", result);
        if (!result) {
            bindRes.put("msg", "绑定失败");
        }

        return bindRes;
    }

    /**
     * 获取验证码 的 请求路径
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpSession session) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            session.setAttribute("verifyCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping("/sendSMS")
    public
    @ResponseBody
    Map<String, Object> sendSMS(HttpServletRequest request, HttpSession session) throws Exception {
        String phone = request.getParameter("phone");
        String captchaId = (String) session.getAttribute("verifyCode");
        String parameter = request.getParameter("verifyCode");
        log.info("Session  verifyCode {}  form verifyCode {}", captchaId, parameter);

        Map<String, Object> res = new HashMap<>();
        res.put("res", Boolean.TRUE);
        if (null == captchaId || !captchaId.equals(parameter)) {
            res.put("res", Boolean.FALSE);
            res.put("msg", "错误的验证码");
        } else {
            String bindCode = JacksonUtil.getRandomNumber(6);
            if (!JacksonUtil.validateMobile(phone)) {
                res.put("res", Boolean.FALSE);
                res.put("msg", "手机号格式错误");
            } else {
//              //TODO for test
//                boolean sendRes = true;
                String content = "您正在绑定NC的消息提醒，验证码：" + bindCode;
                boolean sendRes = smsSender.smsSend(content, phone, String.valueOf(System.currentTimeMillis()));
                if (!sendRes) {
                    res.put("res", Boolean.FALSE);
                    res.put("msg", "发送短信失败");
                } else {
                    log.info("phone  {} , bindCode : {}", phone, bindCode);
                    session.setAttribute("bindCode", bindCode);
                    session.removeAttribute("verifyCode");
                }
            }
        }
        return res;
    }
}
