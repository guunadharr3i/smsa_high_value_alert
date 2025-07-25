package com.smsa.highValueAlerts.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SMSA_THRESHOLD_MASTER")
public class SmsaThresholdMaster implements Serializable{

    @Id
    @Column(name = "SMSA_THRESHOLD_ID", nullable = false)
    private Long thresholdId;

    @Column(name = "SMSA_MSG_CURRENCY", length = 10)
    private String msgCurrency;

    @Column(name = "SMSA_SENDER_BIC", length = 20)
    private String senderBic;

    @Column(name = "SMSA_MSG_TYPE", length = 20)
    private String msgType;

    @Column(name = "SMSA_CATEGORY_A_FROM_AMOUNT", precision = 20, scale = 2)
    private BigDecimal categoryAFromAmount;

    @Column(name = "SMSA_CATEGORY_A_TO_AMOUNT", precision = 20, scale = 2)
    private BigDecimal categoryAToAmount;

    @Column(name = "SMSA_CATEGORY_B_FROM_AMOUNT", precision = 20, scale = 2)
    private BigDecimal categoryBFromAmount;

    @Column(name = "SMSA_CATEGORY_B_TO_AMOUNT", precision = 20, scale = 2)
    private BigDecimal categoryBToAmount;

    @Column(name = "SMSA_CREATED_BY", length = 10)
    private String createdBy;

    @Column(name = "SMSA_CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "SMSA_MODIFIED_BY", length = 10)
    private String modifiedBy;

    @Column(name = "SMSA_MODIFIED_DATE")
    private LocalDate modifiedDate;

    @Column(name = "SMSA_VERIFIED_BY", length = 10)
    private String verifiedBy;

    @Column(name = "SMSA_VERIFIED_DATE")
    private LocalDate verifiedDate;

    @Column(name = "SMSA_STATUS", length = 10)
    private String status;

    // Getters and Setters

    public Long getThresholdId() {
        return thresholdId;
    }

    public void setThresholdId(Long thresholdId) {
        this.thresholdId = thresholdId;
    }

    public String getMsgCurrency() {
        return msgCurrency;
    }

    public void setMsgCurrency(String msgCurrency) {
        this.msgCurrency = msgCurrency;
    }

    public String getSenderBic() {
        return senderBic;
    }

    public void setSenderBic(String senderBic) {
        this.senderBic = senderBic;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public BigDecimal getCategoryAFromAmount() {
        return categoryAFromAmount;
    }

    public void setCategoryAFromAmount(BigDecimal categoryAFromAmount) {
        this.categoryAFromAmount = categoryAFromAmount;
    }

    public BigDecimal getCategoryAToAmount() {
        return categoryAToAmount;
    }

    public void setCategoryAToAmount(BigDecimal categoryAToAmount) {
        this.categoryAToAmount = categoryAToAmount;
    }

    public BigDecimal getCategoryBFromAmount() {
        return categoryBFromAmount;
    }

    public void setCategoryBFromAmount(BigDecimal categoryBFromAmount) {
        this.categoryBFromAmount = categoryBFromAmount;
    }

    public BigDecimal getCategoryBToAmount() {
        return categoryBToAmount;
    }

    public void setCategoryBToAmount(BigDecimal categoryBToAmount) {
        this.categoryBToAmount = categoryBToAmount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
