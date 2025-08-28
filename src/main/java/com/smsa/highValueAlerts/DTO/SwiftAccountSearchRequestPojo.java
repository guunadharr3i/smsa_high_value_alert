/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.DTO;

import java.util.List;

/**
 *
 * @author abcom
 */
public class SwiftAccountSearchRequestPojo {

    private List<Long> id;

    private List<String> io;

    private List<String> senderBIC;

    private List<String> accountNO;

    private List<String> bankName;

    private List<String> location;

    private List<String> messageTyp;

    private List<String> receivedBIC;

    private List<String> currency;

    private List<String> team;

    private List<String> remark;

    private List<String> status;

    /**
     * @return the id
     */
    public List<Long> getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(List<Long> id) {
        this.id = id;
    }

    /**
     * @return the io
     */
    public List<String> getIo() {
        return io;
    }

    /**
     * @param io the io to set
     */
    public void setIo(List<String> io) {
        this.io = io;
    }

    /**
     * @return the senderBIC
     */
    public List<String> getSenderBIC() {
        return senderBIC;
    }

    /**
     * @param senderBIC the senderBIC to set
     */
    public void setSenderBIC(List<String> senderBIC) {
        this.senderBIC = senderBIC;
    }

    /**
     * @return the accountNO
     */
    public List<String> getAccountNO() {
        return accountNO;
    }

    /**
     * @param accountNO the accountNO to set
     */
    public void setAccountNO(List<String> accountNO) {
        this.accountNO = accountNO;
    }

    /**
     * @return the bankName
     */
    public List<String> getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(List<String> bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the location
     */
    public List<String> getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(List<String> location) {
        this.location = location;
    }

    /**
     * @return the messageTyp
     */
    public List<String> getMessageTyp() {
        return messageTyp;
    }

    /**
     * @param messageTyp the messageTyp to set
     */
    public void setMessageTyp(List<String> messageTyp) {
        this.messageTyp = messageTyp;
    }

    /**
     * @return the receivedBIC
     */
    public List<String> getReceivedBIC() {
        return receivedBIC;
    }

    /**
     * @param receivedBIC the receivedBIC to set
     */
    public void setReceivedBIC(List<String> receivedBIC) {
        this.receivedBIC = receivedBIC;
    }

    /**
     * @return the currency
     */
    public List<String> getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    /**
     * @return the team
     */
    public List<String> getTeam() {
        return team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(List<String> team) {
        this.team = team;
    }

    /**
     * @return the remark
     */
    public List<String> getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(List<String> remark) {
        this.remark = remark;
    }

    /**
     * @return the status
     */
    public List<String> getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(List<String> status) {
        this.status = status;
    }

}
