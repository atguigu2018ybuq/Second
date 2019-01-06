package com.hucheng.cfms.service;

import com.hucheng.cfms.entity.MemberDO;

public interface MemberDOService {
	
	int getLoginAcctCount(String loginAcct);
	
	void saveMember(MemberDO memberDO);

	MemberDO getMemberDOByAcct(String loginacc);

	void updateMemberDO(MemberDO memberDO);

	MemberDO getMemberByProcessInstanceId(String processInstanceId);

	MemberDO getMemberByMemberId(String memberId);

	void updateTicketAndMemberAfterCompleteProcess(String processInstanceId, Byte autostatus);
	
}
