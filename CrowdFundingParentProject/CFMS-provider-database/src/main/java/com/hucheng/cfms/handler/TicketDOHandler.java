package com.hucheng.cfms.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.entity.TicketDO;
import com.hucheng.cfms.service.TicketDOService;

@RestController
public class TicketDOHandler {

	@Autowired
	private TicketDOService ticketDOService;

	/**
	 * 通过memberId查询出对应的ticketDO对象
	 */
	@RequestMapping("/get/ticket/by/memberid/remote")
	public ResultVO<TicketDO> getTicketByMemberIdRemote(@RequestParam("memberId") Integer memberId) {
		TicketDO ticketDO = ticketDOService.getTicketByMemberId(memberId);
		return new ResultVO<TicketDO>(ResultVO.SUCCESS, ResultVO.NO_MSG, ticketDO);
	}

	/**
	 * 更改t_ticket表中的pStep并保存验证码code
	 */
	@RequestMapping("/update/ticket/pstep/and/code/remote")
	public ResultVO<String> updateTicketPstepAndCodeRemote(@RequestParam("memberId") Integer memberId,
			@RequestParam("pStep") String pStep, @RequestParam("code") String code) {
		ticketDOService.updateTicketPstepAndCode(memberId, pStep, code);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	}

	/**
	 * 修改t_ticket表的pstep下一个任务
	 */
	@RequestMapping("/update/ticket/pstep/remote")
	public ResultVO<String> updateTicketPstepRemote(@RequestParam("memberId") Integer memberId,
			@RequestParam("pStep") String pStep) {
		ticketDOService.updateTicketPstep(memberId, pStep);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	}

	/**
	 * 调用远程方法根据memberId查询t_ticket表中对应的pstep值
	 */
	@RequestMapping("/get/pstep/by/memberId/remote")
	public ResultVO<String> getPstepByMemberIdRemote(@RequestParam("memberId") Integer memberId) {
		String pstep = ticketDOService.getPstepByMemberId(memberId);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_DATA, pstep);
	}

	/**
	 * 保存流程审批表单ticketDO对象到数据库中
	 */
	@RequestMapping("/save/ticket/remote")
	public ResultVO<String> saveTicketRomote(@RequestBody TicketDO ticketDO) {
		ticketDOService.saveTicket(ticketDO);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	}

}
