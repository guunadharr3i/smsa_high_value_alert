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
public class ReciepientRequestDto {

    private Map<String, String> tokenMap;
    private RecepientDTO recepientDTO;
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
     * @return the recepientDTO
     */
    public RecepientDTO getRecepientDTO() {
        return recepientDTO;
    }

    /**
     * @param recepientDTO the recepientDTO to set
     */
    public void setRecepientDTO(RecepientDTO recepientDTO) {
        this.recepientDTO = recepientDTO;
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
