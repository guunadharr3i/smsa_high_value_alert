package com.smsa.highValueAlerts.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ThresholdDTO {

    private Long thresholdId;
    private String msgCurrency;
    private String senderBic;
    private String msgType;
    private BigDecimal categoryAFromAmount;
    private BigDecimal categoryAToAmount;
    private BigDecimal categoryBFromAmount;
    private BigDecimal categoryBToAmount;
    private String createdBy;
    private LocalDate createdDate;
    private String modifiedBy;
    private LocalDate modifiedDate;
    private String verifiedBy;
    private LocalDate verifiedDate;
    private String action;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
