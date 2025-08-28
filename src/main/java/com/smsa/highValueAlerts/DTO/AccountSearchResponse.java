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
public class AccountSearchResponse {
    
    private List<SmsaAccountPojo> messages;
    private int totalPages;
    private long totalElements;
    private int currentPage;

    /**
     * @return the messages
     */
    public List<SmsaAccountPojo> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<SmsaAccountPojo> messages) {
        this.messages = messages;
    }

    /**
     * @return the totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages the totalPages to set
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return the totalElements
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * @param totalElements the totalElements to set
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
