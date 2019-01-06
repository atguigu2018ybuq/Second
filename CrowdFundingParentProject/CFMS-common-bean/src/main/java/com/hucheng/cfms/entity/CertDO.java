package com.hucheng.cfms.entity;

public class CertDO {
	private Integer id;

	private String name;

	public CertDO() {

	}

	public CertDO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	@Override
	public String toString() {
		return "CertDO [id=" + id + ", name=" + name + "]";
	}
}