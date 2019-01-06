package com.hucheng.cfms.mapper;

import java.util.List;

import com.hucheng.cfms.entity.CertDO;
import com.hucheng.cfms.entity.CertInfoVO;

public interface CertDOMapper {
    int insert(CertDO record);

    List<CertDO> selectAll();

	List<CertDO> selectCertListByAccType(Byte accType);

	List<CertInfoVO> selectCertInfoVOByMemberId(String memberId);
}