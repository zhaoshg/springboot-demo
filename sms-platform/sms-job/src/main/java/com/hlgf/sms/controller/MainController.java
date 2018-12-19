package com.hlgf.sms.controller;


import com.hlgf.sms.model.SmsSendSetting;
import com.hlgf.sms.service.SmsSendSettingService;
import com.hlgf.sms.utils.FreemarkerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class MainController {

    @Value("${admin.username}")
    private String username = "admin";

    @Value("${admin.password}")
    private String password = "123654";

    @Autowired
    private SmsSendSettingService smsSendSettingService;


    @RequestMapping("/login")
    public String login(ModelMap model) {
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        model.addAttribute("error", "");
        return "login";
    }

    @RequestMapping("signin")
    public String sign_in(String username, String password, HttpServletRequest request, ModelMap model) {
        if (username.equals(this.username) && password.equals(this.password)) {
            request.getSession().setAttribute("login", true);
            return "redirect:list";
        } else {
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("error", "用户名密码错误");
            return "login";
        }
    }

    @RequestMapping("list")
    public String findList(ModelMap model, HttpServletRequest request) {
        Object login = request.getSession().getAttribute("login");
        if (login != null && login.toString().equalsIgnoreCase("true")) {
            FreemarkerUtil.initStatics(model);
            List<SmsSendSetting> all = smsSendSettingService.getAll();
            model.addAttribute("all", all);
            return "list";
        } else {
            return "redirect:login";
        }
    }

    @RequestMapping("updateSetting")
    public @ResponseBody
    Map<String, Object> updateSetting(String pkGroup, int state) {
        Map res = new HashMap();
        if (pkGroup == null) {
            res.put("res", false);
            res.put("msg", "公司错误");
        } else {
            boolean result = smsSendSettingService.updateSetting(pkGroup, state);
            if (result) {
                res.put("res", true);
            } else {
                res.put("res", false);
                res.put("msg", "更新错误");
            }
        }
        return res;
    }
}
