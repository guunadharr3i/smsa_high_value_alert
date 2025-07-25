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
public class RecepientFilterPojo {

    private List<Long> smsaRamId;

    private List< String> smsaEmpId;

    private List< String> smsaGeoName;

    private List< String> smsaSenderBic;

    private List< Long> smsaMsgType;

    private List< String> smsaEmpName;

    private List< String> smsaGrade;

    private List< String> smsaCreatedBy;

    private List< String> smsaModifiedBy;

    private List< LocalDate> smsaModifiedDate;

    private List< String> smsaVerifiedBy;

    private List< LocalDate> smsaVerifiedDate;

    /**
     * @return the smsaRamId
     */
    public List<Long> getSmsaRamId() {
        return smsaRamId;
    }

    /**
     * @param smsaRamId the smsaRamId to set
     */
    public void setSmsaRamId(List<Long> smsaRamId) {
        this.smsaRamId = smsaRamId;
    }

    /**
     * @return the smsaEmpId
     */
    public List< String> getSmsaEmpId() {
        return smsaEmpId;
    }

    /**
     * @param smsaEmpId the smsaEmpId to set
     */
    public void setSmsaEmpId(List< String> smsaEmpId) {
        this.smsaEmpId = smsaEmpId;
    }

    /**
     * @return the smsaGeoName
     */
    public List< String> getSmsaGeoName() {
        return smsaGeoName;
    }

    /**
     * @param smsaGeoName the smsaGeoName to set
     */
    public void setSmsaGeoName(List< String> smsaGeoName) {
        this.smsaGeoName = smsaGeoName;
    }

    /**
     * @return the smsaSenderBic
     */
    public List< String> getSmsaSenderBic() {
        return smsaSenderBic;
    }

    /**
     * @param smsaSenderBic the smsaSenderBic to set
     */
    public void setSmsaSenderBic(List< String> smsaSenderBic) {
        this.smsaSenderBic = smsaSenderBic;
    }

    /**
     * @return the smsaMsgType
     */
    public List< Long> getSmsaMsgType() {
        return smsaMsgType;
    }

    /**
     * @param smsaMsgType the smsaMsgType to set
     */
    public void setSmsaMsgType(List< Long> smsaMsgType) {
        this.smsaMsgType = smsaMsgType;
    }

    /**
     * @return the smsaEmpName
     */
    public List< String> getSmsaEmpName() {
        return smsaEmpName;
    }

    /**
     * @param smsaEmpName the smsaEmpName to set
     */
    public void setSmsaEmpName(List< String> smsaEmpName) {
        this.smsaEmpName = smsaEmpName;
    }

    /**
     * @return the smsaGrade
     */
    public List< String> getSmsaGrade() {
        return smsaGrade;
    }

    /**
     * @param smsaGrade the smsaGrade to set
     */
    public void setSmsaGrade(List< String> smsaGrade) {
        this.smsaGrade = smsaGrade;
    }

    /**
     * @return the smsaCreatedBy
     */
    public List< String> getSmsaCreatedBy() {
        return smsaCreatedBy;
    }

    /**
     * @param smsaCreatedBy the smsaCreatedBy to set
     */
    public void setSmsaCreatedBy(List< String> smsaCreatedBy) {
        this.smsaCreatedBy = smsaCreatedBy;
    }

    /**
     * @return the smsaModifiedBy
     */
    public List< String> getSmsaModifiedBy() {
        return smsaModifiedBy;
    }

    /**
     * @param smsaModifiedBy the smsaModifiedBy to set
     */
    public void setSmsaModifiedBy(List< String> smsaModifiedBy) {
        this.smsaModifiedBy = smsaModifiedBy;
    }

    /**
     * @return the smsaModifiedDate
     */
    public List< LocalDate> getSmsaModifiedDate() {
        return smsaModifiedDate;
    }

    /**
     * @param smsaModifiedDate the smsaModifiedDate to set
     */
    public void setSmsaModifiedDate(List< LocalDate> smsaModifiedDate) {
        this.smsaModifiedDate = smsaModifiedDate;
    }

    /**
     * @return the smsaVerifiedBy
     */
    public List< String> getSmsaVerifiedBy() {
        return smsaVerifiedBy;
    }

    /**
     * @param smsaVerifiedBy the smsaVerifiedBy to set
     */
    public void setSmsaVerifiedBy(List< String> smsaVerifiedBy) {
        this.smsaVerifiedBy = smsaVerifiedBy;
    }

    /**
     * @return the smsaVerifiedDate
     */
    public List< LocalDate> getSmsaVerifiedDate() {
        return smsaVerifiedDate;
    }

    /**
     * @param smsaVerifiedDate the smsaVerifiedDate to set
     */
    public void setSmsaVerifiedDate(List< LocalDate> smsaVerifiedDate) {
        this.smsaVerifiedDate = smsaVerifiedDate;
    }
}
