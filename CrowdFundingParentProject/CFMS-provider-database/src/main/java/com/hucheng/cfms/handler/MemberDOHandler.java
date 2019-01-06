package com.hucheng.cfms.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hucheng.cfms.entity.MemberDO;
import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.service.MemberDOService;
import com.hucheng.crowdfunding.constant.ErrorMessage;
import com.hucheng.crowdfunding.util.CrowdfundingStringUtils;

//provider不能返回页面给consumer,所以provider上每个方法都应该使用@ResponseBody注解
@RestController
public class MemberDOHandler {

	@Autowired
	private MemberDOService memberDOService;

	/**
	 * 通过流程实例的processInstanceId查出对应的Member对象并修改Member对象的autoStatus
	 */
	@RequestMapping("/update/ticket/and/member/after/complete/process")
	public ResultVO<String> updateTicketAndMemberAfterCompleteProcessRemote(
			@RequestParam("processInstanceId") String processInstanceId, @RequestParam("autostatus") Byte autostatus) {

		memberDOService.updateTicketAndMemberAfterCompleteProcess(processInstanceId, autostatus);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	}

	/**
	 * 通过processInstanceId查询出对应的MemberDO对象
	 */
	@RequestMapping("/get/member/by/processinstanceid/remote")
	public ResultVO<MemberDO> getMemberByProcessInstanceIdRemote(
			@RequestParam("processInstanceId") String processInstanceId) {
		MemberDO memberDO = memberDOService.getMemberByProcessInstanceId(processInstanceId);
		return new ResultVO<MemberDO>(ResultVO.SUCCESS, ResultVO.NO_MSG, memberDO);
	}

	/**
	 * 提供远程更新MemberDO(实际上是为了更新Member对象的auto_status)
	 */
	@RequestMapping("/update/member/remote")
	public ResultVO<String> UpdateMemberRemote(@RequestBody MemberDO memberDO) {
		memberDOService.updateMemberDO(memberDO);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	}

	/**
	 * 提供通过会员对应的会员名查询出在数据库中的会员对象
	 */
	@RequestMapping("/get/member/by/loginacc")
	public MemberDO getUserByLoginacct(@RequestBody MemberDO memberDOFromForm) {
		// 1.从memberDOFromForm中获取账号和密码
		String loginacc = memberDOFromForm.getLoginAcc();
		// 2.根据传来的loginacc查询数据库
		MemberDO memberDOFromDB = memberDOService.getMemberDOByAcct(loginacc);
		return memberDOFromDB;
	}

	@RequestMapping("/get/login/acct/count")
	public ResultVO<Integer> getLoginAcctCountRemote(@RequestParam("loginAcct") String loginAcct) {
		try {
			// 正常操作返回的数据
			int count = memberDOService.getLoginAcctCount(loginAcct);
			return new ResultVO<Integer>(ResultVO.SUCCESS, ResultVO.NO_MSG, count);
		} catch (Exception e) {
			e.printStackTrace();
			// 发生异常返回的数据
			return new ResultVO<Integer>(ResultVO.FAILED, e.getMessage(), null);
		}
	}

	@RequestMapping("/save/member/remote")
	public ResultVO<String> saveMemverRemote(@RequestBody MemberDO memberDO) {
		// 1.尝试获取当前Member对象中的密码数据
		String loginPwd = memberDO.getLoginPwd();

		// 2.验证密码是否有效
		if (!CrowdfundingStringUtils.stringCheck(loginPwd)) {
			return new ResultVO<String>(ResultVO.FAILED, ErrorMessage.MD5_SOURCE_MISSING, ResultVO.NO_DATA);
		}

		// 3.对密码进行加密
		loginPwd = CrowdfundingStringUtils.md5(loginPwd);
		memberDO.setLoginPwd(loginPwd);

		try {
			memberDOService.saveMember(memberDO);
			return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVO<String>(ResultVO.FAILED, e.getMessage(), ResultVO.NO_DATA);
		}
	}

}
