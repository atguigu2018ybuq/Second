package com.hucheng.cfms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hucheng.cfms.entity.TicketDO;

public interface TicketDOMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(TicketDO record);

	TicketDO selectByPrimaryKey(Integer id);

	List<TicketDO> selectAll();

	int updateByPrimaryKey(TicketDO record);

	String selectPstepByMemberId(Integer memberId);

	void updateTicketPstep(@Param("memberId") Integer memberId, @Param("pStep") String pStep);

	void updateTicketPstepAndCode(@Param("memberId") Integer memberId, @Param("pStep") String pStep,
			@Param("code") String code);

	TicketDO selectTicketByMemberId(@Param("memberId") Integer memberId);
}