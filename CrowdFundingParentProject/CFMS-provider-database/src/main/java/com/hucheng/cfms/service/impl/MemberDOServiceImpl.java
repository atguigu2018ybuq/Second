package com.hucheng.cfms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hucheng.cfms.entity.MemberDO;
import com.hucheng.cfms.mapper.MemberDOMapper;
import com.hucheng.cfms.service.MemberDOService;

@Service
@Transactional(readOnly = true) // 类上声明只读属性后，相当于每个方法都有这个属性
public class MemberDOServiceImpl implements MemberDOService {

	@Autowired
	private MemberDOMapper memberDOMapper;

	@Override
	public int getLoginAcctCount(String loginAcct) {
		return memberDOMapper.selectLoginAcctCount(loginAcct);
	}

	// 对于增删改这些非只读的方法需要另外声明事物属性
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveMember(MemberDO memberDO) {
		memberDOMapper.insert(memberDO);
	}

	@Override
	public MemberDO getMemberDOByAcct(String loginacc) {
		return memberDOMapper.selectMemberDOByAcct(loginacc);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateMemberDO(MemberDO memberDO) {
		memberDOMapper.updateMemberDO(memberDO);
	}

	@Override
	public MemberDO getMemberByProcessInstanceId(String processInstanceId) {
		return memberDOMapper.selectMemberByProcessInstanceId(processInstanceId);
	}

	@Override
	public MemberDO getMemberByMemberId(String memberId) {
		return memberDOMapper.selectByPrimaryKey(memberId);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateTicketAndMemberAfterCompleteProcess(String processInstanceId, Byte autostatus) {
		memberDOMapper.updateTicketAndMemberAfterCompleteProcess(processInstanceId, autostatus);
	}

}
