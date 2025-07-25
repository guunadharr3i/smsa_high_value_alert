package com.smsa.highValueAlerts.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SMSA_RECEPIENT_MASTER")
public class SmsaRecepientMaster implements Serializable{

    @Id
    @Column(name = "SMSA_RAM_ID", nullable = false)
    private Long smsaRamId;

    @Column(name = "SMSA_EMP_ID", length = 10)
    private String smsaEmpId;

    @Column(name = "SMSA_GEO_NAME", length = 20)
    private String smsaGeoName;

    @Column(name = "SMSA_SENDER_BIC", length = 20)
    private String smsaSenderBic;

    @Column(name = "SMSA_MSG_TYPE")
    private Long smsaMsgType;

    @Column(name = "SMSA_EMP_NAME", length = 150)
    private String smsaEmpName;

    @Column(name = "SMSA_GRADE", length = 10)
    private String smsaGrade;

    @Column(name = "SMSA_CREATED_BY", length = 10)
    private String smsaCreatedBy;

    @Column(name = "SMSA_MODIFIED_BY", length = 10)
    private String smsaModifiedBy;

    @Column(name = "SMSA_MODIFIED_DATE")
    private LocalDate smsaModifiedDate;

    @Column(name = "SMSA_VERIFIED_BY", length = 10)
    private String smsaVerifiedBy;

    @Column(name = "SMSA_VERIFIED_DATE")
    private LocalDate smsaVerifiedDate;

    @Column(name = "SMSA_STATUS", length = 10)
    private String smsaStatus;

    // Getters and Setters

    public Long getSmsaRamId() {
        return smsaRamId;
    }

    public void setSmsaRamId(Long smsaRamId) {
        this.smsaRamId = smsaRamId;
    }

    public String getSmsaEmpId() {
        return smsaEmpId;
    }

    public void setSmsaEmpId(String smsaEmpId) {
        this.smsaEmpId = smsaEmpId;
    }

    public String getSmsaGeoName() {
        return smsaGeoName;
    }

    public void setSmsaGeoName(String smsaGeoName) {
        this.smsaGeoName = smsaGeoName;
    }

    public String getSmsaSenderBic() {
        return smsaSenderBic;
    }

    public void setSmsaSenderBic(String smsaSenderBic) {
        this.smsaSenderBic = smsaSenderBic;
    }

    public Long getSmsaMsgType() {
        return smsaMsgType;
    }

    public void setSmsaMsgType(Long smsaMsgType) {
        this.smsaMsgType = smsaMsgType;
    }

    public String getSmsaEmpName() {
        return smsaEmpName;
    }

    public void setSmsaEmpName(String smsaEmpName) {
        this.smsaEmpName = smsaEmpName;
    }

    public String getSmsaGrade() {
        return smsaGrade;
    }

    public void setSmsaGrade(String smsaGrade) {
        this.smsaGrade = smsaGrade;
    }

    public String getSmsaCreatedBy() {
        return smsaCreatedBy;
    }

    public void setSmsaCreatedBy(String smsaCreatedBy) {
        this.smsaCreatedBy = smsaCreatedBy;
    }

    public String getSmsaModifiedBy() {
        return smsaModifiedBy;
    }

    public void setSmsaModifiedBy(String smsaModifiedBy) {
        this.smsaModifiedBy = smsaModifiedBy;
    }

    public LocalDate getSmsaModifiedDate() {
        return smsaModifiedDate;
    }

    public void setSmsaModifiedDate(LocalDate smsaModifiedDate) {
        this.smsaModifiedDate = smsaModifiedDate;
    }

    public String getSmsaVerifiedBy() {
        return smsaVerifiedBy;
    }

    public void setSmsaVerifiedBy(String smsaVerifiedBy) {
        this.smsaVerifiedBy = smsaVerifiedBy;
    }

    public LocalDate getSmsaVerifiedDate() {
        return smsaVerifiedDate;
    }

    public void setSmsaVerifiedDate(LocalDate smsaVerifiedDate) {
        this.smsaVerifiedDate = smsaVerifiedDate;
    }

    public String getSmsaStatus() {
        return smsaStatus;
    }

    public void setSmsaStatus(String smsaStatus) {
        this.smsaStatus = smsaStatus;
    }
}