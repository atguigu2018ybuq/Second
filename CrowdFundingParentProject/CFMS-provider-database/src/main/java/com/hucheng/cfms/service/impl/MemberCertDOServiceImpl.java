package com.hucheng.cfms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hucheng.cfms.entity.MemberCertDO;
import com.hucheng.cfms.mapper.MemberCertDOMapper;
import com.hucheng.cfms.service.MemberCertDOService;
@Service
@Transactional(readOnly = true)
public class MemberCertDOServiceImpl implements MemberCertDOService {

	@Autowired
	private MemberCertDOMapper memberCertDOMapper;
	
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	@Override
	public void saveMemberCertDOList(List<MemberCertDO> memberCertDOList) {
		memberCertDOMapper.insertMemberCertDOList(memberCertDOList);
	}
}
