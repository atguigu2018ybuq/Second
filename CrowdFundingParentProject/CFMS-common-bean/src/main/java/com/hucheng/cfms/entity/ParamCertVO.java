package com.hucheng.cfms.entity;

import org.springframework.web.multipart.MultipartFile;

public class ParamCertVO {

	// 资质文件的id
	private Integer certId;
	// 资质文件对应的文件
	private MultipartFile certFile;

	public ParamCertVO() {

	}

	public ParamCertVO(Integer certId, MultipartFile certFile) {

		this.certId = certId;
		this.certFile = certFile;
	}

	public Integer getCertId() {
		return certId;
	}

	public void setCertId(Integer certId) {
		this.certId = certId;
	}

	public MultipartFile getCertFile() {
		return certFile;
	}

	public void setCertFile(MultipartFile certFile) {
		this.certFile = certFile;
	}

	@Override
	public String toString() {
		return "ParamCertVO [certId=" + certId + ", certFile=" + certFile + "]";
	}
}
