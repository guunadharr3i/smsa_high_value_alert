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
public class ThresholdSearchRequest {
    private Map<String, String> tokenMap;
    private ThresholdFilterPojo thresholdFilterPojo;

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

    /**
     * @return the thresholdFilterPojo
     */
    public ThresholdFilterPojo getThresholdFilterPojo() {
        return thresholdFilterPojo;
    }

    /**
     * @param thresholdFilterPojo the thresholdFilterPojo to set
     */
    public void setThresholdFilterPojo(ThresholdFilterPojo thresholdFilterPojo) {
        this.thresholdFilterPojo = thresholdFilterPojo;
    }
    
}
