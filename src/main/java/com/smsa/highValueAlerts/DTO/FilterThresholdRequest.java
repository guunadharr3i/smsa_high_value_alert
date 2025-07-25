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
public class FilterThresholdRequest {
     private Map<String,String> tokenMap;
    private ThresholdFilterPojo filter;

    /**
     * @return the tokenMap
     */
    public Map<String,String> getTokenMap() {
        return tokenMap;
    }

    /**
     * @param tokenMap the tokenMap to set
     */
    public void setTokenMap(Map<String,String> tokenMap) {
        this.tokenMap = tokenMap;
    }

    /**
     * @return the filter
     */
    public ThresholdFilterPojo getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(ThresholdFilterPojo filter) {
        this.filter = filter;
    }
}
