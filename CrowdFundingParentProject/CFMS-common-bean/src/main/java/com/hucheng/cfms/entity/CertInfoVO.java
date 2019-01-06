package com.hucheng.cfms.entity;

public class CertInfoVO {

	private String certName;
	private String iconPath;

	public CertInfoVO() {

	}

	public CertInfoVO(String certName, String iconPath) {
		this.certName = certName;
		this.iconPath = iconPath;
	}

	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@Override
	public String toString() {
		return "CertInfoVO [certName=" + certName + ", iconPath=" + iconPath + "]";
	}

}
