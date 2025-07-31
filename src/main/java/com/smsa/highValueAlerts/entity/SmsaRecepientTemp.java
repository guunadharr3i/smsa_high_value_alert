package com.smsa.highValueAlerts.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "SMSA_RECEPIENT_TEMP")
public class SmsaRecepientTemp implements Serializable {

    @Id
    @GeneratedValue(generator = "smsa-ram-id-generator")
    @GenericGenerator(
            name = "smsa-ram-id-generator",
            strategy = "com.smsa.highValueAlerts.utils.UseExistingOrGenerateId",
            parameters = @Parameter(name = "sequence_name", value = "SMSA_RAM_SEQ")
    )
    @Column(name = "SMSA_RAM_ID", nullable = false)
    private Long smsaRamId;

    @Column(name = "SMSA_REC_EMP_ID", length = 10)
    private String recEmpId;

    @Column(name = "SMSA_REC_EMAIL_ID")
    private String recEmailId;

    @Column(name = "SMSA_REC_EMP_NAME", length = 150)
    private String recEmpName;

    @Column(name = "SMSA_REC_GEO_NAME", length = 20)
    private String recGeoName;

    @Column(name = "SMSA_REC_SENDER_BIC", length = 20)
    private String recSenderBic;

    @Column(name = "SMSA_REC_MSG_TYPE")
    private String recMsgType;

    @Column(name = "SMSA_REC_GRADE", length = 10)
    private String recGrade;

    @Column(name = "SMSA_REC_CREATED_BY", length = 10)
    private String recCreatedBy;

    @Column(name = "SMSA_REC_CREATED_DATE")
    private LocalDate recCreatedDate;

    @Column(name = "SMSA_REC_MODIFIED_BY", length = 10)
    private String recModifiedBy;

    @Column(name = "SMSA_REC_MODIFIED_DATE")
    private LocalDate recModifiedDate;

    @Column(name = "SMSA_REC_VERIFIED_BY", length = 10)
    private String recVerifiedBy;

    @Column(name = "SMSA_REC_VERIFIED_DATE")
    private LocalDate recVerifiedDate;

    @Column(name = "SMSA_REC_CATEGORY")
    private String recCategory;

    @Column(name = "SMSA_REC_CC_EMPID")
    private String recCCEmpId;

    @Column(name = "SMSA_REC_CC_MAILID")
    private String recCCMailId;

    @Column(name = "SMSA_REC_ACTION", length = 10)
    private String smsaRecOperation;

    // Getters and Setters
    
    /**
     * @return the smsaRamId
     */
    public Long getSmsaRamId() {
        return smsaRamId;
    }

    /**
     * @param smsaRamId the smsaRamId to set
     */
    public void setSmsaRamId(Long smsaRamId) {
        this.smsaRamId = smsaRamId;
}

    /**
     * @return the recEmpId
     */
    public String getRecEmpId() {
        return recEmpId;
    }

    /**
     * @param recEmpId the recEmpId to set
     */
    public void setRecEmpId(String recEmpId) {
        this.recEmpId = recEmpId;
    }

    /**
     * @return the recEmailId
     */
    public String getRecEmailId() {
        return recEmailId;
    }

    /**
     * @param recEmailId the recEmailId to set
     */
    public void setRecEmailId(String recEmailId) {
        this.recEmailId = recEmailId;
    }

    /**
     * @return the recEmpName
     */
    public String getRecEmpName() {
        return recEmpName;
    }

    /**
     * @param recEmpName the recEmpName to set
     */
    public void setRecEmpName(String recEmpName) {
        this.recEmpName = recEmpName;
    }

    /**
     * @return the recGeoName
     */
    public String getRecGeoName() {
        return recGeoName;
    }

    /**
     * @param recGeoName the recGeoName to set
     */
    public void setRecGeoName(String recGeoName) {
        this.recGeoName = recGeoName;
    }

    /**
     * @return the recSenderBic
     */
    public String getRecSenderBic() {
        return recSenderBic;
    }

    /**
     * @param recSenderBic the recSenderBic to set
     */
    public void setRecSenderBic(String recSenderBic) {
        this.recSenderBic = recSenderBic;
    }

    /**
     * @return the recMsgType
     */
    public String getRecMsgType() {
        return recMsgType;
    }

    /**
     * @param recMsgType the recMsgType to set
     */
    public void setRecMsgType(String recMsgType) {
        this.recMsgType = recMsgType;
    }

    /**
     * @return the recGrade
     */
    public String getRecGrade() {
        return recGrade;
    }

    /**
     * @param recGrade the recGrade to set
     */
    public void setRecGrade(String recGrade) {
        this.recGrade = recGrade;
    }

    /**
     * @return the recCreatedBy
     */
    public String getRecCreatedBy() {
        return recCreatedBy;
    }

    /**
     * @param recCreatedBy the recCreatedBy to set
     */
    public void setRecCreatedBy(String recCreatedBy) {
        this.recCreatedBy = recCreatedBy;
    }

    /**
     * @return the recCreatedDate
     */
    public LocalDate getRecCreatedDate() {
        return recCreatedDate;
    }

    /**
     * @param recCreatedDate the recCreatedDate to set
     */
    public void setRecCreatedDate(LocalDate recCreatedDate) {
        this.recCreatedDate = recCreatedDate;
    }

    /**
     * @return the recModifiedBy
     */
    public String getRecModifiedBy() {
        return recModifiedBy;
    }

    /**
     * @param recModifiedBy the recModifiedBy to set
     */
    public void setRecModifiedBy(String recModifiedBy) {
        this.recModifiedBy = recModifiedBy;
    }

    /**
     * @return the recModifiedDate
     */
    public LocalDate getRecModifiedDate() {
        return recModifiedDate;
    }

    /**
     * @param recModifiedDate the recModifiedDate to set
     */
    public void setRecModifiedDate(LocalDate recModifiedDate) {
        this.recModifiedDate = recModifiedDate;
    }

    /**
     * @return the recVerifiedBy
     */
    public String getRecVerifiedBy() {
        return recVerifiedBy;
    }

    /**
     * @param recVerifiedBy the recVerifiedBy to set
     */
    public void setRecVerifiedBy(String recVerifiedBy) {
        this.recVerifiedBy = recVerifiedBy;
    }

    /**
     * @return the recVerifiedDate
     */
    public LocalDate getRecVerifiedDate() {
        return recVerifiedDate;
    }

    /**
     * @param recVerifiedDate the recVerifiedDate to set
     */
    public void setRecVerifiedDate(LocalDate recVerifiedDate) {
        this.recVerifiedDate = recVerifiedDate;
    }

    /**
     * @return the recCategory
     */
    public String getRecCategory() {
        return recCategory;
    }

    /**
     * @param recCategory the recCategory to set
     */
    public void setRecCategory(String recCategory) {
        this.recCategory = recCategory;
    }

    /**
     * @return the recCCEmpId
     */
    public String getRecCCEmpId() {
        return recCCEmpId;
    }

    /**
     * @param recCCEmpId the recCCEmpId to set
     */
    public void setRecCCEmpId(String recCCEmpId) {
        this.recCCEmpId = recCCEmpId;
    }

    /**
     * @return the recCCMailId
     */
    public String getRecCCMailId() {
        return recCCMailId;
    }

    /**
     * @param recCCMailId the recCCMailId to set
     */
    public void setRecCCMailId(String recCCMailId) {
        this.recCCMailId = recCCMailId;
    }

    /**
     * @return the smsaRecOperation
     */
    public String getSmsaRecOperation() {
        return smsaRecOperation;
    }

    /**
     * @param smsaRecOperation the smsaRecOperation to set
     */
    public void setSmsaRecOperation(String smsaRecOperation) {
        this.smsaRecOperation = smsaRecOperation;
    }


    
}
