package com.hucheng.cfms.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hucheng.cfms.entity.ApprovalDetailVO;
import com.hucheng.cfms.entity.CertInfoVO;
import com.hucheng.cfms.entity.MemberDO;
import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.service.CertDOService;
import com.hucheng.cfms.service.MemberDOService;

@RestController
public class ApprovalDetailVOHandler {

	@Autowired
	private MemberDOService memberDOService;
	
	@Autowired
	private CertDOService certDOService;
	
	/**
	 * 点击查看详细信息可以查看会员申请流程的数据和图
	 */
	@RequestMapping("/get/process/approval/detail/remote")
	public ResultVO<ApprovalDetailVO> getProcessApprovalDetailRemote(@RequestParam("memberId") String memberId) {
		MemberDO memberDO = memberDOService.getMemberByMemberId(memberId);
		List<CertInfoVO> certInfoVOList =certDOService.getCertInfoVOByMemberId(memberId);
		ApprovalDetailVO approvalDetailVO = new ApprovalDetailVO(memberDO, certInfoVOList);
		return new ResultVO<ApprovalDetailVO>(ResultVO.SUCCESS, ResultVO.NO_MSG, approvalDetailVO);
	}
}
