package com.smsa.highValueAlerts.DTO;

public class SmsaAccountReq {

    private String actionType;
    private SmsaAccountPojo smsaAccount;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public SmsaAccountPojo getSmsaAccount() {
        return smsaAccount;
    }

    public void setSmsaAccount(SmsaAccountPojo smsaAccount) {
        this.smsaAccount = smsaAccount;
    }
}
