package com.hucheng.cfms.entity;

import java.util.List;

/**
 * @author Lenovo
 *
 */
public class ParamCertListVO {

	private List<ParamCertVO> paramList;

	public ParamCertListVO() {

	}

	public ParamCertListVO(List<ParamCertVO> paramList) {

		this.paramList = paramList;
	}

	public List<ParamCertVO> getParamList() {
		return paramList;
	}

	public void setParamList(List<ParamCertVO> paramList) {
		this.paramList = paramList;
	}

	@Override
	public String toString() {
		return "ParamCertListVO [paramList=" + paramList + "]";
	}
}
