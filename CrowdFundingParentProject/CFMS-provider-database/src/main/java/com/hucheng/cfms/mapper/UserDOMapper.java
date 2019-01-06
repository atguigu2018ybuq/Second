package com.hucheng.cfms.mapper;

import com.hucheng.cfms.entity.UserDO;

public interface UserDOMapper {
	
	// 根据用户名来查询用户
    UserDO selectUserDOByAcct(String loginacct);
}