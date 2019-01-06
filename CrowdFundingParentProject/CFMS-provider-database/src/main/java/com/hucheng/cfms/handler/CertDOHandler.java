package com.hucheng.cfms.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hucheng.cfms.entity.CertDO;
import com.hucheng.cfms.entity.ResultVO;
import com.hucheng.cfms.service.CertDOService;

@RestController
public class CertDOHandler {

	@Autowired
	private CertDOService certDOService;

	/**
	 * 通过acctype账户类型来获取对应的需要提供的证明资质文件名
	 */
	@RequestMapping("/get/cert/list/by/acctype/remote")
	public ResultVO<List<CertDO>> getCertListByAccTypeRemote(@RequestParam("accType") Byte accType) {
		List<CertDO> certList = certDOService.getCertListByAccType(accType);
		return new ResultVO<List<CertDO>>(ResultVO.SUCCESS, ResultVO.NO_MSG, certList);
	}
}
