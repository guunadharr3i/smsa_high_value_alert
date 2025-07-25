package com.smsa.highValueAlerts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smsa.highValueAlerts.DTO.ThresholdDTO;
import com.smsa.highValueAlerts.entity.SmsaThresholdMaster;
import com.smsa.highValueAlerts.entity.SmsaThresholdTemp;
import com.smsa.highValueAlerts.repository.ThresholdMasterRepo;
import com.smsa.highValueAlerts.repository.ThresholdTempRepo;
import java.util.stream.Collectors;

@Service
public class SmsaThresholdTempService {

    @Autowired
    private ThresholdTempRepo thresholdTempRepo;

    @Autowired
    private ThresholdMasterRepo thresholdMasterRepo;

    public String addThresholdTempData(ThresholdDTO thresholdDTO) {

        // Check uniqueness of the 4-column combination
        boolean existsInMs = thresholdMasterRepo.existsByUniqueCombo(
                thresholdDTO.getMsgCurrency(),
                thresholdDTO.getSenderBic(),
                thresholdDTO.getMsgType()
        );

        if (existsInMs) {
            return String.format(
                    "Threshold already exists in masterTable with Currency = '%s', SenderBIC = '%s', MsgType = '%s'.",
                    thresholdDTO.getMsgCurrency(),
                    thresholdDTO.getSenderBic(),
                    thresholdDTO.getMsgType());
        }
        boolean existsTemp = thresholdTempRepo.existsByUniqueCombo(
                thresholdDTO.getMsgCurrency(),
                thresholdDTO.getSenderBic(),
                thresholdDTO.getMsgType());

        if (existsTemp) {
            return String.format(
                    "Threshold data already waiting for approval with Currency = '%s', SenderBIC = '%s', MsgType = '%s'.",
                    thresholdDTO.getMsgCurrency(),
                    thresholdDTO.getSenderBic(),
                    thresholdDTO.getMsgType());

        }

        // Proceed with insert
        SmsaThresholdTemp smsaThresholdTemp = buildPojoToEntityCombo(thresholdDTO);
        thresholdTempRepo.save(smsaThresholdTemp);

        return "Recipient added successfully and went for approval";
    }

    public String updateThresholdData(ThresholdDTO thresholdDTO) {
        if (thresholdDTO.getThresholdId() != null) {
            boolean existsInMs = thresholdMasterRepo.existsByUniqueCombo(
                    thresholdDTO.getMsgCurrency(),
                    thresholdDTO.getSenderBic(),
                    thresholdDTO.getMsgType());
            boolean existsTemp = thresholdTempRepo.existsByUniqueCombo(
                    thresholdDTO.getMsgCurrency(),
                    thresholdDTO.getSenderBic(),
                    thresholdDTO.getMsgType());

            if (existsInMs || existsTemp) {
                SmsaThresholdTemp smsaThresholdTemp = buildPojoToEntityCombo(thresholdDTO);
                thresholdTempRepo.save(smsaThresholdTemp);
                return "Updated Successfully ,Went for approval";
            }
        }
        return "Id not found to update";
    }

    public String deleteThresholdByEmpId(Long smsaThresholdID) {
        if (smsaThresholdID == null) {
            return "smsaThreshold must not be null";
        }

        Optional<SmsaThresholdMaster> existing = thresholdMasterRepo.findById(smsaThresholdID);
        if (existing.isPresent()) {
            thresholdTempRepo.deleteBySmsaThresholdId(smsaThresholdID);
            return "Recipient deleted successfully";
        } else {
            return "Recipient with smsaThresholdID " + smsaThresholdID + " not found";
        }
    }

    public List<ThresholdDTO> getThresholdTempData() {
        List<SmsaThresholdTemp> data = thresholdTempRepo.findAll();
        List<ThresholdDTO> pojoList = data.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());
        return pojoList;
    }

    private ThresholdDTO mapToPojo(SmsaThresholdTemp entity) {
        ThresholdDTO pojo = new ThresholdDTO();
        pojo.setThresholdId(entity.getThresholdId());
        pojo.setMsgCurrency(entity.getMsgCurrency());
        pojo.setSenderBic(entity.getSenderBic());
        pojo.setMsgType(entity.getMsgType());
        pojo.setCategoryAToAmount(entity.getCategoryAToAmount());
        pojo.setCategoryAFromAmount(entity.getCategoryAFromAmount());
        pojo.setCategoryBFromAmount(entity.getCategoryAFromAmount());
        pojo.setCategoryBToAmount(entity.getCategoryBToAmount());
        pojo.setCreatedBy(entity.getCreatedBy());
        pojo.setCreatedDate(entity.getCreatedDate());
        pojo.setModifiedBy(entity.getModifiedBy());
        pojo.setModifiedDate(entity.getModifiedDate());
        pojo.setVerifiedBy(entity.getVerifiedBy());
        pojo.setVerifiedDate(entity.getVerifiedDate());
        pojo.setAction(entity.getAction());

        return pojo;
    }

    public SmsaThresholdTemp buildPojoToEntityCombo(ThresholdDTO thresholdDTO) {
        SmsaThresholdTemp smsaThresholdTemp = new SmsaThresholdTemp();

        smsaThresholdTemp.setThresholdId(thresholdDTO.getThresholdId());
        smsaThresholdTemp.setMsgCurrency(thresholdDTO.getMsgCurrency());
        smsaThresholdTemp.setSenderBic(thresholdDTO.getSenderBic());
        smsaThresholdTemp.setMsgType(thresholdDTO.getMsgType());
        smsaThresholdTemp.setCategoryAFromAmount(thresholdDTO.getCategoryAFromAmount());
        smsaThresholdTemp.setCategoryAToAmount(thresholdDTO.getCategoryAToAmount());
        smsaThresholdTemp.setCategoryBFromAmount(thresholdDTO.getCategoryBFromAmount());
        smsaThresholdTemp.setCategoryBToAmount(thresholdDTO.getCategoryBToAmount());
        smsaThresholdTemp.setCreatedBy(thresholdDTO.getCreatedBy());
        smsaThresholdTemp.setCreatedDate(thresholdDTO.getCreatedDate());
        smsaThresholdTemp.setModifiedBy(thresholdDTO.getModifiedBy());
        smsaThresholdTemp.setModifiedDate(thresholdDTO.getModifiedDate());
        smsaThresholdTemp.setVerifiedBy(thresholdDTO.getVerifiedBy());
        smsaThresholdTemp.setVerifiedDate(thresholdDTO.getVerifiedDate());
        smsaThresholdTemp.setAction(thresholdDTO.getAction());
        return smsaThresholdTemp;
    }

}
