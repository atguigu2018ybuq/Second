package com.hucheng.cfms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hucheng.cfms.entity.CertDO;
import com.hucheng.cfms.entity.CertInfoVO;
import com.hucheng.cfms.mapper.CertDOMapper;
import com.hucheng.cfms.service.CertDOService;

@Service
@Transactional(readOnly = true)
public class CertDOServiceImpl implements CertDOService {

	@Autowired
	private CertDOMapper certDOMapper;

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public List<CertDO> getCertListByAccType(Byte accType) {
		return certDOMapper.selectCertListByAccType(accType);
	}

	@Override
	public List<CertInfoVO> getCertInfoVOByMemberId(String memberId) {
		return certDOMapper.selectCertInfoVOByMemberId(memberId);
	}

}
