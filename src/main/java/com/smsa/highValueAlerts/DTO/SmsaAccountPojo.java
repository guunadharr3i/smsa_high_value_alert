package com.smsa.highValueAlerts.DTO;

public class SmsaAccountPojo {

    private Long id;
    private String io;
    private String senderBIC;
    private String accountNO;
    private String bankName;
    private String location;
    private Boolean isConfirm;
    private String messageTyp;
    private String receivedBIC;
    private String currency;
    private String team;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIo() {
        return io;
    }

    public void setIo(String io) {
        this.io = io;
    }

    public String getSenderBIC() {
        return senderBIC;
    }

    public void setSenderBIC(String senderBIC) {
        this.senderBIC = senderBIC;
    }

    public String getAccountNO() {
        return accountNO;
    }

    public void setAccountNO(String accountNO) {
        this.accountNO = accountNO;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(Boolean isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getMessageTyp() {
        return messageTyp;
    }

    public void setMessageTyp(String messageTyp) {
        this.messageTyp = messageTyp;
    }

    public String getReceivedBIC() {
        return receivedBIC;
    }

    public void setReceivedBIC(String receivedBIC) {
        this.receivedBIC = receivedBIC;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
