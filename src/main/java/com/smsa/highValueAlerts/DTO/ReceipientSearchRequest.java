/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.DTO;

import java.util.Map;

/**
 *
 * @author abcom
 */
public class ReceipientSearchRequest {
    private RecepientFilterPojo recepientFilterPojo;
    private Map<String, String> tokenMap;

    /**
     * @return the recepientFilterPojo
     */
    public RecepientFilterPojo getRecepientFilterPojo() {
        return recepientFilterPojo;
    }

    /**
     * @param recepientFilterPojo the recepientFilterPojo to set
     */
    public void setRecepientFilterPojo(RecepientFilterPojo recepientFilterPojo) {
        this.recepientFilterPojo = recepientFilterPojo;
    }

    /**
     * @return the tokenMap
     */
    public Map<String, String> getTokenMap() {
        return tokenMap;
    }

    /**
     * @param tokenMap the tokenMap to set
     */
    public void setTokenMap(Map<String, String> tokenMap) {
        this.tokenMap = tokenMap;
    }
    
}
