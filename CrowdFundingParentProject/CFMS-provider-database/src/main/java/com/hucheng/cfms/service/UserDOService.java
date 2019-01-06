package com.hucheng.cfms.service;

import com.hucheng.cfms.entity.UserDO;

public interface UserDOService {
	
	// 根据用户名来查询用户
	UserDO getUserDOByAcct(String loginacct);
	    
}
