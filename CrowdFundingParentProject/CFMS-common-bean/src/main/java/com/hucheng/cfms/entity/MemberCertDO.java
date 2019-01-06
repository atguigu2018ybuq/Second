package com.hucheng.cfms.entity;

public class MemberCertDO {
    private Integer id;

    private Integer memberid;

    private Integer certid;

    private String iconpath;

    public MemberCertDO() {
    	
	}

	public MemberCertDO(Integer id, Integer memberid, Integer certid, String iconpath) {
		this.id = id;
		this.memberid = memberid;
		this.certid = certid;
		this.iconpath = iconpath;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public Integer getCertid() {
        return certid;
    }

    public void setCertid(Integer certid) {
        this.certid = certid;
    }

    public String getIconpath() {
        return iconpath;
    }

    public void setIconpath(String iconpath) {
        this.iconpath = iconpath == null ? null : iconpath.trim();
    }

	@Override
	public String toString() {
		return "MemberCertDO [id=" + id + ", memberid=" + memberid + ", certid=" + certid + ", iconpath=" + iconpath
				+ "]";
	}
}