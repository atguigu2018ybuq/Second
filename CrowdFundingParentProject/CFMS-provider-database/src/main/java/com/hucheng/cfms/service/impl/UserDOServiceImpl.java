package com.hucheng.cfms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hucheng.cfms.entity.UserDO;
import com.hucheng.cfms.mapper.UserDOMapper;
import com.hucheng.cfms.service.UserDOService;

@Service
@Transactional(readOnly = true)//类上声明只读属性后，相当于每个方法都有这个属性
public class UserDOServiceImpl implements UserDOService{
	
	@Autowired
	private UserDOMapper userDOMapper;
	
	// 根据用户名来查询用户
	@Override
	public UserDO getUserDOByAcct(String loginacct) {
		return userDOMapper.selectUserDOByAcct(loginacct);
	}

}
