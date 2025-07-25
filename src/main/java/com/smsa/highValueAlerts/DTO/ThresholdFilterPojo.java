/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.DTO;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author abcom
 */
public class ThresholdFilterPojo {

    private List<Long> thresholdId;
    private List<String> msgCurrency;
    private List<String> senderBic;
    private List<String> msgType;
    private List<Long> categoryAFromAmount;
    private List<Long> categoryAToAmount;
    private List<Long> categoryBFromAmount;
    private List<Long> categoryBToAmount;
    private List<String> createdBy;
    private List<LocalDate> createdDate;
    private List<String> modifiedBy;
    private List<LocalDate> modifiedDate;
    private List<String> verifiedBy;
    private List<LocalDate> verifiedDate;

    /**
     * @return the thresholdId
     */
    public List<Long> getThresholdId() {
        return thresholdId;
    }

    /**
     * @param thresholdId the thresholdId to set
     */
    public void setThresholdId(List<Long> thresholdId) {
        this.thresholdId = thresholdId;
    }

    /**
     * @return the msgCurrency
     */
    public List<String> getMsgCurrency() {
        return msgCurrency;
    }

    /**
     * @param msgCurrency the msgCurrency to set
     */
    public void setMsgCurrency(List<String> msgCurrency) {
        this.msgCurrency = msgCurrency;
    }

    /**
     * @return the senderBic
     */
    public List<String> getSenderBic() {
        return senderBic;
    }

    /**
     * @param senderBic the senderBic to set
     */
    public void setSenderBic(List<String> senderBic) {
        this.senderBic = senderBic;
    }

    /**
     * @return the msgType
     */
    public List<String> getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(List<String> msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the categoryAFromAmount
     */
    public List<Long> getCategoryAFromAmount() {
        return categoryAFromAmount;
    }

    /**
     * @param categoryAFromAmount the categoryAFromAmount to set
     */
    public void setCategoryAFromAmount(List<Long> categoryAFromAmount) {
        this.categoryAFromAmount = categoryAFromAmount;
    }

    /**
     * @return the categoryAToAmount
     */
    public List<Long> getCategoryAToAmount() {
        return categoryAToAmount;
    }

    /**
     * @param categoryAToAmount the categoryAToAmount to set
     */
    public void setCategoryAToAmount(List<Long> categoryAToAmount) {
        this.categoryAToAmount = categoryAToAmount;
    }

    /**
     * @return the categoryBFromAmount
     */
    public List<Long> getCategoryBFromAmount() {
        return categoryBFromAmount;
    }

    /**
     * @param categoryBFromAmount the categoryBFromAmount to set
     */
    public void setCategoryBFromAmount(List<Long> categoryBFromAmount) {
        this.categoryBFromAmount = categoryBFromAmount;
    }

    /**
     * @return the categoryBToAmount
     */
    public List<Long> getCategoryBToAmount() {
        return categoryBToAmount;
    }

    /**
     * @param categoryBToAmount the categoryBToAmount to set
     */
    public void setCategoryBToAmount(List<Long> categoryBToAmount) {
        this.categoryBToAmount = categoryBToAmount;
    }

    /**
     * @return the createdBy
     */
    public List<String> getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(List<String> createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdDate
     */
    public List<LocalDate> getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(List<LocalDate> createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the modifiedBy
     */
    public List<String> getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(List<String> modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modifiedDate
     */
    public List<LocalDate> getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @param modifiedDate the modifiedDate to set
     */
    public void setModifiedDate(List<LocalDate> modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * @return the verifiedBy
     */
    public List<String> getVerifiedBy() {
        return verifiedBy;
    }

    /**
     * @param verifiedBy the verifiedBy to set
     */
    public void setVerifiedBy(List<String> verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    /**
     * @return the verifiedDate
     */
    public List<LocalDate> getVerifiedDate() {
        return verifiedDate;
    }

    /**
     * @param verifiedDate the verifiedDate to set
     */
    public void setVerifiedDate(List<LocalDate> verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

}
