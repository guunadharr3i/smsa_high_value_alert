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
public class FilterReceipientRequest {
    private Map<String,String> tokenMap;
    private RecepientFilterPojo filter;

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
    public RecepientFilterPojo getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(RecepientFilterPojo filter) {
        this.filter = filter;
    }
    
}
