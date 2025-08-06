package com.smsa.highValueAlerts.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SMSA_ACCOUNT", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ACCOUNT_NO")
})
public class SmsaAccountTemp implements Serializable {

    @Id
    @GeneratedValue(generator = "smsa-ram-id-generator")
    @GenericGenerator(name = "smsa-ram-id-generator", strategy = "com.smsa.highValueAlerts.utils.UseExistingOrGenerateId", parameters = @Parameter(name = "sequence_name", value = "SMSA_RAM_SEQ"))
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "IO")
    private String io;

    @Column(name = "SENDER_BIC")
    private String senderBIC;

    @Column(name = "ACCOUNT_NO", unique = true)
    private String accountNO;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "IS_CONFIRM")
    private Boolean isConfirm;

    @Column(name = "MESSAGE_TYP")
    private String messageTyp;

    @Column(name = "RECEIVED_BIC")
    private String receivedBIC;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TEAM")
    private String team;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ACTIONTYPE")
    private String actionType;

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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
