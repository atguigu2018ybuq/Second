package com.hucheng.cfms.entity;

public class MemberDO {
    private Integer memberId;

    private String loginAcc;

    private String loginPwd;

    private String nickName;

    private String realName;

    private String cardNum;

    private String phoneNum;

    private String emailAddr;

    private Byte authStatus;

    private Byte accType;

    private Byte company;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getLoginAcc() {
        return loginAcc;
    }

    public void setLoginAcc(String loginAcc) {
        this.loginAcc = loginAcc == null ? null : loginAcc.trim();
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd == null ? null : loginPwd.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum == null ? null : cardNum.trim();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr == null ? null : emailAddr.trim();
    }

    public Byte getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Byte authStatus) {
        this.authStatus = authStatus;
    }

    public Byte getAccType() {
        return accType;
    }

    public void setAccType(Byte accType) {
        this.accType = accType;
    }

    public Byte getCompany() {
        return company;
    }

    public void setCompany(Byte company) {
        this.company = company;
    }
}