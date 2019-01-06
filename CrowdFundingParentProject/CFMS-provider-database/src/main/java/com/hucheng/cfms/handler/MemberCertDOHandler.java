package com.hucheng.cfms.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hucheng.cfms.entity.MemberCertDO;
import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.service.MemberCertDOService;

@RestController
public class MemberCertDOHandler {

	@Autowired
	private MemberCertDOService memberCertDOService;
	
	@RequestMapping("/save/membercertdo/list/remote")
	public ResultVO<String> saveMemberCertDOListRemote(@RequestBody List<MemberCertDO> memberCertDOList) {
		memberCertDOService.saveMemberCertDOList(memberCertDOList);
		return new ResultVO<String>(ResultVO.SUCCESS, ResultVO.NO_MSG, ResultVO.NO_DATA);
	};
}
