package com.smsa.highValueAlerts.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smsa.highValueAlerts.DTO.ThresholdDTO;
import com.smsa.highValueAlerts.entity.SmsaThresholdMaster;
import com.smsa.highValueAlerts.entity.SmsaThresholdTemp;
import com.smsa.highValueAlerts.repository.ThresholdMasterRepo;
import com.smsa.highValueAlerts.repository.ThresholdTempRepo;

@Service
@Transactional
public class SmsaThresholdTempService {

    private static final Logger logger = LogManager.getLogger(SmsaThresholdTempService.class);

    @Autowired
    private ThresholdTempRepo thresholdTempRepo;

    @Autowired
    private ThresholdMasterRepo thresholdMasterRepo;

    public String addThresholdTempData(ThresholdDTO thresholdDTO) {
        logger.info("Attempting to add threshold temp data: {}", thresholdDTO);
        try {
            boolean existsInMs = thresholdMasterRepo.existsByMsgCurrencyAndSenderBicAndMsgType(
                    thresholdDTO.getMsgCurrency(),
                    thresholdDTO.getSenderBic(),
                    thresholdDTO.getMsgType());

            if (existsInMs) {
                logger.warn("Duplicate entry in master for: {}", thresholdDTO);
                return String.format(
                        "Threshold already exists in masterTable with Currency = '%s', SenderBIC = '%s', MsgType = '%s'.",
                        thresholdDTO.getMsgCurrency(),
                        thresholdDTO.getSenderBic(),
                        thresholdDTO.getMsgType());
            }

            boolean existsTemp = thresholdTempRepo.existsByMsgCurrencyAndSenderBicAndMsgType(
                    thresholdDTO.getMsgCurrency(),
                    thresholdDTO.getSenderBic(),
                    thresholdDTO.getMsgType());

            if (existsTemp) {
                logger.warn("Duplicate temp threshold awaiting approval for: {}", thresholdDTO);
                return String.format(
                        "Threshold data already waiting for approval with Currency = '%s', SenderBIC = '%s', MsgType = '%s'.",
                        thresholdDTO.getMsgCurrency(),
                        thresholdDTO.getSenderBic(),
                        thresholdDTO.getMsgType());
            }

            SmsaThresholdTemp smsaThresholdTemp = buildPojoToEntityCombo(thresholdDTO);
            thresholdTempRepo.save(smsaThresholdTemp);
            logger.info("Threshold temp data saved for approval: {}", smsaThresholdTemp);

            return "Threshold Data added successfully and went for approval";

        } catch (Exception e) {
            logger.error("Error while adding threshold temp data: {}", e.getMessage(), e);
            return "An error occurred while adding threshold temp data.";
        }
    }

    public String updateThresholdData(ThresholdDTO thresholdDTO) {
        logger.info("Updating threshold data: {}", thresholdDTO);
        try {
            if (thresholdDTO.getThresholdId() != null) {
                boolean existsInMs = thresholdMasterRepo.existsByMsgCurrencyAndSenderBicAndMsgType(
                        thresholdDTO.getMsgCurrency(),
                        thresholdDTO.getSenderBic(),
                        thresholdDTO.getMsgType());
                boolean existsTemp = thresholdTempRepo.existsByMsgCurrencyAndSenderBicAndMsgType(
                        thresholdDTO.getMsgCurrency(),
                        thresholdDTO.getSenderBic(),
                        thresholdDTO.getMsgType());

                if (existsInMs || existsTemp) {
                    SmsaThresholdTemp smsaThresholdTemp = buildPojoToEntityCombo(thresholdDTO);
                    thresholdTempRepo.save(smsaThresholdTemp);
                    logger.info("Threshold temp data updated for approval: {}", smsaThresholdTemp);
                    return "Updated Successfully, Went for approval";
                } else {
                    logger.warn("Threshold ID not found to update: {}", thresholdDTO.getThresholdId());
                }
            }
        } catch (Exception e) {
            logger.error("Error while updating threshold data: {}", e.getMessage(), e);
        }
        return "Id not found to update";
    }

    public String deleteThresholdByEmpId(Long smsaThresholdID) {
        logger.info("Attempting to delete threshold with ID: {}", smsaThresholdID);
        if (smsaThresholdID == null) {
            logger.warn("Threshold ID is null");
            return "smsaThreshold must not be null";
        }

        try {
            Optional<SmsaThresholdMaster> existing = thresholdMasterRepo.findById(smsaThresholdID);
            if (existing.isPresent()) {
                thresholdTempRepo.deleteByThresholdId(smsaThresholdID);
                logger.info("Deleted threshold temp data for ID: {}", smsaThresholdID);
                return "Recipient deleted successfully";
            } else {
                logger.warn("No recipient found with ID: {}", smsaThresholdID);
                return "Recipient with smsaThresholdID " + smsaThresholdID + " not found";
            }
        } catch (Exception e) {
            logger.error("Error while deleting threshold data: {}", e.getMessage(), e);
            return "An error occurred while deleting recipient data.";
        }
    }

    public List<ThresholdDTO> getThresholdTempData() {
        logger.info("Fetching all threshold temp data");
        try {
            List<SmsaThresholdTemp> data = thresholdTempRepo.findAll();
            List<ThresholdDTO> pojoList = data.stream()
                    .map(this::mapToPojo)
                    .collect(Collectors.toList());
            logger.info("Fetched {} threshold temp records", pojoList.size());
            return pojoList;
        } catch (Exception e) {
            logger.error("Error fetching threshold temp data: {}", e.getMessage(), e);
            return null;
        }
    }
     public List<ThresholdDTO> getThresholdTempData(String createdBy) {
        logger.info("Fetching all threshold temp data");
        try {
            List<SmsaThresholdTemp> data = thresholdTempRepo.findByCreatedByNot(createdBy);
            List<ThresholdDTO> pojoList = data.stream()
                    .map(this::mapToPojo)
                    .collect(Collectors.toList());
            logger.info("Fetched {} threshold temp records", pojoList.size());
            return pojoList;
        } catch (Exception e) {
            logger.error("Error fetching threshold temp data: {}", e.getMessage(), e);
            return null;
        }
    }

    private ThresholdDTO mapToPojo(SmsaThresholdTemp entity) {
        ThresholdDTO pojo = new ThresholdDTO();
        try {
            pojo.setThresholdId(entity.getThresholdId());
            pojo.setMsgCurrency(entity.getMsgCurrency());
            pojo.setSenderBic(entity.getSenderBic());
            pojo.setMsgType(entity.getMsgType());
            pojo.setCategoryAToAmount(entity.getCategoryAToAmount());
            pojo.setCategoryAFromAmount(entity.getCategoryAFromAmount());
            pojo.setCategoryBFromAmount(entity.getCategoryAFromAmount()); // Possible mistake here
            pojo.setCategoryBToAmount(entity.getCategoryBToAmount());
            pojo.setCreatedBy(entity.getCreatedBy());
            pojo.setCreatedDate(entity.getCreatedDate());
            pojo.setModifiedBy(entity.getModifiedBy());
            pojo.setModifiedDate(entity.getModifiedDate());
            pojo.setVerifiedBy(entity.getVerifiedBy());
            pojo.setVerifiedDate(entity.getVerifiedDate());
            pojo.setAction(entity.getAction());
        } catch (Exception e) {
            logger.error("Error mapping entity to DTO: {}", e.getMessage(), e);
        }
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

    public SmsaThresholdMaster buildTempToMaster(ThresholdDTO thresholdDTO) {
        SmsaThresholdMaster smsaThresholdMaster = new SmsaThresholdMaster();
        smsaThresholdMaster.setThresholdId(thresholdDTO.getThresholdId());
        smsaThresholdMaster.setMsgCurrency(thresholdDTO.getMsgCurrency());
        smsaThresholdMaster.setSenderBic(thresholdDTO.getSenderBic());
        smsaThresholdMaster.setMsgType(thresholdDTO.getMsgType());
        smsaThresholdMaster.setCategoryAFromAmount(thresholdDTO.getCategoryAFromAmount());
        smsaThresholdMaster.setCategoryAToAmount(thresholdDTO.getCategoryAToAmount());
        smsaThresholdMaster.setCategoryBFromAmount(thresholdDTO.getCategoryBFromAmount());
        smsaThresholdMaster.setCategoryBToAmount(thresholdDTO.getCategoryBToAmount());
        smsaThresholdMaster.setCreatedBy(thresholdDTO.getCreatedBy());
        smsaThresholdMaster.setCreatedDate(thresholdDTO.getCreatedDate());
        smsaThresholdMaster.setModifiedBy(thresholdDTO.getModifiedBy());
        smsaThresholdMaster.setModifiedDate(thresholdDTO.getModifiedDate());
        smsaThresholdMaster.setVerifiedBy(thresholdDTO.getVerifiedBy());
        smsaThresholdMaster.setVerifiedDate(thresholdDTO.getVerifiedDate());
        smsaThresholdMaster.setStatus("Active");
        return smsaThresholdMaster;
    }

    public String approveRejectThresholdData(ThresholdDTO thresholdDTO, String action) {
        logger.info("Threshold approval process started for ID: {}, Action: {}", thresholdDTO.getThresholdId(), action);
        try {
            SmsaThresholdMaster srm = buildTempToMaster(thresholdDTO);
            SmsaThresholdTemp stt = buildPojoToEntityCombo(thresholdDTO);

            if ("Approved".equalsIgnoreCase(action)) {
                thresholdMasterRepo.save(srm);
                thresholdTempRepo.delete(stt);
                logger.info("Threshold approved and moved to master table: {}", srm);
                return "Saved Successfully in master";
            } else {
                thresholdTempRepo.delete(stt);
                logger.info("Threshold rejected and removed from temp: {}", stt);
                return "Rejected Successfully";
            }
        } catch (Exception e) {
            logger.error("Error while processing threshold approval/rejection: {}", e.getMessage(), e);
            return "An error occurred while processing the approval.";
        }
    }
}
