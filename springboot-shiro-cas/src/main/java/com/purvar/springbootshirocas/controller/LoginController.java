package com.purvar.springbootshirocas.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class LoginController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/login")
	public String login(String userName, String password) {
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
			// token.setRememberMe(true); //记住我
			try {
				currentUser.login(token);
			} catch (UnknownAccountException uae) {
				log.info("用户不存在 " + token.getPrincipal());
			} catch (IncorrectCredentialsException ice) {
				log.info("密码不正确： " + token.getPrincipal() + "!");
			} catch (LockedAccountException lae) {
				log.info("账号被锁定： " + token.getPrincipal() + " is locked.  ");
			} catch (AuthenticationException ae) { // ... catch more exceptions here (maybe custom ones
													// specific to your application?
				System.out.println("登录失败" + ae);
			}
		}

		return "index";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();

		return "login";
	}
	@RequestMapping("test")
	@ResponseBody
	public String test(){
		return "this is a test";
	}

}
