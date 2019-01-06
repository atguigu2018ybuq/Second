package com.hucheng.cfms.service;

import com.hucheng.cfms.entity.TicketDO;

public interface TicketDOService {

	void saveTicket(TicketDO ticketDO);

	String getPstepByMemberId(Integer memberId);

	void updateTicketPstep(Integer memberId, String pStep);

	void updateTicketPstepAndCode(Integer memberId, String pStep, String code);

	TicketDO getTicketByMemberId(Integer memberId);

}
