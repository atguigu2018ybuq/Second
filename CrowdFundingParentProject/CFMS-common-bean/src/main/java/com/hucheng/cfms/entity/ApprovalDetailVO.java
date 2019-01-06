package com.hucheng.cfms.entity;

import java.util.List;

public class ApprovalDetailVO {

	private MemberDO memberDO;
	private List<CertInfoVO> certInfoVOList;

	public ApprovalDetailVO() {

	}

	public ApprovalDetailVO(MemberDO memberDO, List<CertInfoVO> certInfoVOList) {
		this.memberDO = memberDO;
		this.certInfoVOList = certInfoVOList;
	}

	public MemberDO getMemberDO() {
		return memberDO;
	}

	public void setMemberDO(MemberDO memberDO) {
		this.memberDO = memberDO;
	}

	public List<CertInfoVO> getCertInfoVOList() {
		return certInfoVOList;
	}

	public void setCertInfoVOList(List<CertInfoVO> certInfoVOList) {
		this.certInfoVOList = certInfoVOList;
	}

	@Override
	public String toString() {
		return "ApprovalDetailVO [memberDO=" + memberDO + ", certInfoVOList=" + certInfoVOList + "]";
	}

}
