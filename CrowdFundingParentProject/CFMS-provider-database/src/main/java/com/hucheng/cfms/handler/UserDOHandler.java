package com.hucheng.cfms.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hucheng.cfms.entity.UserDO;
import com.hucheng.cfms.service.UserDOService;

//provider不能返回页面给consumer,所以provider上每个方法都应该使用@ResponseBody注解
@RestController
public class UserDOHandler {

	@Autowired
	private UserDOService userDOService;

	/**
	 * 提供通过用户名查询出在数据库中的用户对象
	 */
	@RequestMapping("/get/user/by/loginacct")
	public UserDO getUserByLoginacct(@RequestBody UserDO userDOFromForm) {
		// 1.从userDoFromForm中获取账号和密码
		String loginacct = userDOFromForm.getLoginacct();
		// 2.根据传来的loginacct查询数据库
		UserDO userDOFromDB = userDOService.getUserDOByAcct(loginacct);
		return userDOFromDB;
	}

}
