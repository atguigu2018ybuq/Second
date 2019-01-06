package com.hucheng.cfms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hucheng.cfms.entity.TicketDO;
import com.hucheng.cfms.mapper.TicketDOMapper;
import com.hucheng.cfms.service.TicketDOService;

@Service
@Transactional(readOnly = true)
public class TicketDOServiceImpl implements TicketDOService {

	@Autowired
	private TicketDOMapper ticketDOMapper;

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveTicket(TicketDO ticketDO) {
		ticketDOMapper.insert(ticketDO);
	}

	@Override
	public String getPstepByMemberId(Integer memberId) {
		return ticketDOMapper.selectPstepByMemberId(memberId);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateTicketPstep(Integer memberId, String pStep) {
		ticketDOMapper.updateTicketPstep(memberId, pStep);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateTicketPstepAndCode(Integer memberId, String pStep, String code) {
		ticketDOMapper.updateTicketPstepAndCode(memberId, pStep, code);
	}

	@Override
	public TicketDO getTicketByMemberId(Integer memberId) {
		return ticketDOMapper.selectTicketByMemberId(memberId);
	}

}
