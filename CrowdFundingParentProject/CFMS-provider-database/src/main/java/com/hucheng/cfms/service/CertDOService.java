package com.hucheng.cfms.service;

import java.util.List;

import com.hucheng.cfms.entity.CertDO;
import com.hucheng.cfms.entity.CertInfoVO;

public interface CertDOService {
	
	List<CertDO> getCertListByAccType(Byte accType);

	List<CertInfoVO> getCertInfoVOByMemberId(String memberId);
}
