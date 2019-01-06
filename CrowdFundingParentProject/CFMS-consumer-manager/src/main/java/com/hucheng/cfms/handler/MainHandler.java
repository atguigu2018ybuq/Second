package com.hucheng.cfms.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainHandler {
	
	@RequestMapping("/manager/main")
	public String toManageMainPage() {
		return "main";
	}
}
