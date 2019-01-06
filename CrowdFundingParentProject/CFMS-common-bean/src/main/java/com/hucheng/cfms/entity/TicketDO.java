package com.hucheng.cfms.entity;

/**
 * @author Lenovo
 *
 */
public class TicketDO {
	private Integer id;

	private Integer memberid;

	private String piid;

	private String status;

	private String authcode;

	private String pstep;

	public TicketDO() {

	}

	public TicketDO(Integer id, Integer memberid, String piid, String status, String authcode, String pstep) {
		this.id = id;
		this.memberid = memberid;
		this.piid = piid;
		this.status = status;
		this.authcode = authcode;
		this.pstep = pstep;
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

	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid == null ? null : piid.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode == null ? null : authcode.trim();
	}

	public String getPstep() {
		return pstep;
	}

	public void setPstep(String pstep) {
		this.pstep = pstep == null ? null : pstep.trim();
	}

	@Override
	public String toString() {
		return "TicketDO [id=" + id + ", memberid=" + memberid + ", piid=" + piid + ", status=" + status + ", authcode="
				+ authcode + ", pstep=" + pstep + "]";
	}

}