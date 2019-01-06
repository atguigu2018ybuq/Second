package com.hucheng.cfms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hucheng.cfms.entity.MemberDO;

public interface MemberDOMapper {
	int deleteByPrimaryKey(Integer memberId);

	int insert(MemberDO record);

	MemberDO selectByPrimaryKey(String memberId);

	List<MemberDO> selectAll();

	int updateByPrimaryKey(MemberDO record);

	int selectLoginAcctCount(String loginAcct);

	MemberDO selectMemberDOByAcct(String loginacc);

	void updateMemberDO(MemberDO memberDO);

	MemberDO selectMemberByProcessInstanceId(String processInstanceId);

	void updateTicketAndMemberAfterCompleteProcess(@Param("processInstanceId") String processInstanceId,
			@Param("autostatus") Byte autostatus);

}