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
public class ThresholdRequestDTO {

    private Map<String, String> tokenMap;
    private ThresholdDTO thresholdDTO;
    private String operation;

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
     * @return the thresholdDTO
     */
    public ThresholdDTO getThresholdDTO() {
        return thresholdDTO;
    }

    /**
     * @param thresholdDTO the thresholdDTO to set
     */
    public void setThresholdDTO(ThresholdDTO thresholdDTO) {
        this.thresholdDTO = thresholdDTO;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
}
