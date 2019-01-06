package com.hucheng.cfms.handler;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalHandler {

	/**
	 * 前往用户的主页面
	 */
//	@RequestMapping("/user/main")
//	public String toUserMainPage() {
//		return "management/main";
//	}
	/**
	 * 前往会员的主页面
	 */
//	@RequestMapping("/member/main")
//	public String toManageMainPage() {
//		return "member/main";
//	}

	/**
	 * 前往注册页面
	 */
//	@RequestMapping("/member/register")
//	public String toMemberRegisterPage() {
//		return "info/register";
//	}

	/**
	 * 前往登录页面
	 */
//	@RequestMapping("/member/login")
//	public String toLoginPage() {
//		return "info/login";
//	}

	/**
	 * 前往首页
	 */
//	@RequestMapping("/index")
//	public String toIndexPage() {
//		return "main";
//	}
	/**
	 * 注销
	 */
	@RequestMapping("/user/logout")
	public String doLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/index";
	}
}
