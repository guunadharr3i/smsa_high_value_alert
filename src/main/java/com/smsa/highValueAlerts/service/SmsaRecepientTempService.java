package com.smsa.highValueAlerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smsa.highValueAlerts.DTO.RecepientDTO;
import com.smsa.highValueAlerts.entity.SmsaRecepientMaster;
import com.smsa.highValueAlerts.entity.SmsaRecepientTemp;
import com.smsa.highValueAlerts.repository.RecepientMasterRepo;
import com.smsa.highValueAlerts.repository.RecepientTempRepo;
import java.util.Arrays;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Service
@Transactional
public class SmsaRecepientTempService {

    private static final Logger logger = LogManager.getLogger(SmsaRecepientTempService.class);

    @Autowired
    private RecepientTempRepo recepientTempRepo;

    @Autowired
    private RecepientMasterRepo recepientMasterRepo;

    public String addRecepientTempData(RecepientDTO recepientDTO) {
        try {
            logger.info("Checking if recipient exists in master or temp table.");
            boolean existsInMs = recepientMasterRepo.existsByRecEmpIdAndRecGeoNameAndRecSenderBicAndRecMsgType(
                    recepientDTO.getRecCCEmpId(),
                    recepientDTO.getRecGeoName(),
                    recepientDTO.getRecSenderBic(),
                    recepientDTO.getRecMsgType()
            );

            if (existsInMs) {
                logger.warn("Recipient already exists in master table: {}", recepientDTO);
                return String.format(
                        "Recipient already exists in masterTable with EmpId = '%s', GeoName = '%s', SenderBIC = '%s', MsgType = '%s'.",
                        recepientDTO.getRecCCEmpId(),
                        recepientDTO.getRecGeoName(),
                        recepientDTO.getRecSenderBic(),
                        recepientDTO.getRecMsgType()
                );
            }

            boolean existsTemp = recepientTempRepo.existsByRecEmpIdAndRecGeoNameAndRecSenderBicAndRecMsgType(
                    recepientDTO.getRecCCEmpId(),
                    recepientDTO.getRecGeoName(),
                    recepientDTO.getRecSenderBic(),
                    recepientDTO.getRecMsgType()
            );

            if (existsTemp) {
                logger.warn("Recipient already exists in temp table: {}", recepientDTO);
                return String.format(
                        "Recipient data already waiting for approval with EmpId = '%s', GeoName = '%s', SenderBIC = '%s', MsgType = '%s'.",
                        recepientDTO.getRecCCEmpId(),
                        recepientDTO.getRecGeoName(),
                        recepientDTO.getRecSenderBic(),
                        recepientDTO.getRecMsgType()
                );
            }

            SmsaRecepientTemp smsaRecepientTemp = buildPojoToEntityCombo(recepientDTO);
            recepientTempRepo.save(smsaRecepientTemp);
            logger.info("Recipient saved successfully to temp table.");
            return "Recipient added successfully and went for approval";
        } catch (Exception e) {
            logger.error("Error while adding recipient temp data: {}", e.getMessage(), e);
            return "Failed to add recipient data due to internal error.";
        }
    }

    public String updateRecieptData(RecepientDTO recepientDTO) {
        try {
            if (recepientDTO.getSmsaRamId() != null) {
                Optional<SmsaRecepientMaster> existsInMs = recepientMasterRepo.findById(recepientDTO.getSmsaRamId());

                if (existsInMs.isPresent()) {
                    SmsaRecepientTemp smsaRecepientTemp = buildPojoToEntityCombo(recepientDTO);
                    recepientTempRepo.saveAndFlush(smsaRecepientTemp);
                    logger.info("Recipient updated and sent for approval: {}", recepientDTO);
                    return "Updated Successfully ,Went for approval";
                }
            }
            logger.warn("Recipient ID not found for update: {}", recepientDTO.getSmsaRamId());
            return "Id not found to update";
        } catch (Exception e) {
            logger.error("Error updating recipient data: {}", e.getMessage(), e);
            return "Failed to update recipient data due to internal error.";
        }
    }

    public List<SmsaRecepientTemp> getFullTempData() {
        return recepientTempRepo.findAll();
    }

    public String deleteRecepientByEmpId(Long smsaRamId) {
        try {
           

            Optional<SmsaRecepientMaster> existing = recepientMasterRepo.findById(smsaRamId);
            if (existing.isPresent()) {
                // Convert to SmsaRecepientTemp
                SmsaRecepientTemp temp = buildMasterToTempCombo(existing.get());
                recepientTempRepo.save(temp);
                logger.info("Recipient moved to temp and deleted from master with smsaRamId: {}", smsaRamId);
                return "Recipient Delete Request went for approval";
            } else {
                logger.warn("Recipient with smsaRamId {} not found", smsaRamId);
                return "Recipient with smsaRamId " + smsaRamId + " not found";
            }
        } catch (Exception e) {
            logger.error("Error deleting recipient: {}", e.getMessage(), e);
            return "Failed to delete recipient due to internal error.";
        }
    }

    public List<RecepientDTO> getRecepientTempData(String recEmpId) {
        try {
            logger.info("Fetching all recipient temp data.");
            List<SmsaRecepientTemp> data = recepientTempRepo.findByRecCreatedByNot(recEmpId);
            List<RecepientDTO> pojoList = data.stream().map(this::mapToPojo).collect(Collectors.toList());
            logger.info("Fetched {} records from recipient temp.", pojoList.size());
            return pojoList;
        } catch (Exception e) {
            logger.error("Error fetching recipient temp data: {}", e.getMessage(), e);
            return Arrays.asList();
        }
    }

    public List<RecepientDTO> getRecepientTempData() {
        try {
            logger.info("Fetching all recipient temp data.");
            List<SmsaRecepientTemp> data = recepientTempRepo.findAll();
            List<RecepientDTO> pojoList = data.stream().map(this::mapToPojo).collect(Collectors.toList());
            logger.info("Fetched {} records from recipient temp.", pojoList.size());
            return pojoList;
        } catch (Exception e) {
            logger.error("Error fetching recipient temp data: {}", e.getMessage(), e);
            return Arrays.asList();
        }
    }

    private RecepientDTO mapToPojo(SmsaRecepientTemp entity) {
        RecepientDTO pojo = new RecepientDTO();
        try {
            pojo.setSmsaRamId(entity.getSmsaRamId());
            pojo.setRecEmpId(entity.getRecEmpId());
            pojo.setRecEmailId(entity.getRecEmailId());
            pojo.setRecEmpName(entity.getRecEmpName());
            pojo.setRecGeoName(entity.getRecGeoName());
            pojo.setRecSenderBic(entity.getRecSenderBic());
            pojo.setRecMsgType(entity.getRecMsgType());
            pojo.setRecGrade(entity.getRecGrade());
            pojo.setRecCreatedBy(entity.getRecCreatedBy());
            pojo.setRecCreatedDate(entity.getRecCreatedDate());
            pojo.setRecModifiedBy(entity.getRecModifiedBy());
            pojo.setRecModifiedDate(entity.getRecModifiedDate());
            pojo.setRecVerifiedBy(entity.getRecVerifiedBy());
            pojo.setRecVerifiedDate(entity.getRecVerifiedDate());
            pojo.setRecCategory(entity.getRecCategory());
            pojo.setRecCCEmpId(entity.getRecCCEmpId());
            pojo.setRecCCMailId(entity.getRecCCMailId());
            pojo.setSmsaRecOperation(entity.getSmsaRecOperation());
        } catch (Exception e) {
            logger.error("Error mapping entity to DTO: {}", e.getMessage(), e);
        }
        return pojo;
    }

    public SmsaRecepientTemp buildPojoToEntityCombo(RecepientDTO entity) {
        SmsaRecepientTemp pojo = new SmsaRecepientTemp();
        try {
            pojo.setSmsaRamId(entity.getSmsaRamId());
            pojo.setRecEmpId(entity.getRecEmpId());
            pojo.setRecEmailId(entity.getRecEmailId());
            pojo.setRecEmpName(entity.getRecEmpName());
            pojo.setRecGeoName(entity.getRecGeoName());
            pojo.setRecSenderBic(entity.getRecSenderBic());
            pojo.setRecMsgType(entity.getRecMsgType());
            pojo.setRecGrade(entity.getRecGrade());
            pojo.setRecCreatedBy(entity.getRecCreatedBy());
            pojo.setRecCreatedDate(entity.getRecCreatedDate());
            pojo.setRecModifiedBy(entity.getRecModifiedBy());
            pojo.setRecModifiedDate(entity.getRecModifiedDate());
            pojo.setRecVerifiedBy(entity.getRecVerifiedBy());
            pojo.setRecVerifiedDate(entity.getRecVerifiedDate());
            pojo.setRecCategory(entity.getRecCategory());
            pojo.setRecCCEmpId(entity.getRecCCEmpId());
            pojo.setRecCCMailId(entity.getRecCCMailId());
            pojo.setSmsaRecOperation(entity.getSmsaRecOperation());
        } catch (Exception e) {
            logger.error("Error mapping entity to DTO: {}", e.getMessage(), e);
        }
        return pojo;
    }

    public SmsaRecepientTemp buildMasterToTempCombo(SmsaRecepientMaster entity) {
        SmsaRecepientTemp pojo = new SmsaRecepientTemp();
        try {
            pojo.setSmsaRamId(entity.getSmsaRamId());
            pojo.setRecEmpId(entity.getRecEmpId());
            pojo.setRecEmailId(entity.getRecEmailId());
            pojo.setRecEmpName(entity.getRecEmpName());
            pojo.setRecGeoName(entity.getRecGeoName());
            pojo.setRecSenderBic(entity.getRecSenderBic());
            pojo.setRecMsgType(entity.getRecMsgType());
            pojo.setRecGrade(entity.getRecGrade());
            pojo.setRecCreatedBy(entity.getRecCreatedBy());
            pojo.setRecCreatedDate(entity.getRecCreatedDate());
            pojo.setRecModifiedBy(entity.getRecModifiedBy());
            pojo.setRecModifiedDate(entity.getRecModifiedDate());
            pojo.setRecVerifiedBy(entity.getRecVerifiedBy());
            pojo.setRecVerifiedDate(entity.getRecVerifiedDate());
            pojo.setRecCategory(entity.getRecCategory());
            pojo.setRecCCEmpId(entity.getRecCCEmpId());
            pojo.setRecCCMailId(entity.getRecCCMailId());
            pojo.setSmsaRecOperation("DELETE");
        } catch (Exception e) {
            logger.error("Error mapping entity to DTO: {}", e.getMessage(), e);
        }
        return pojo;
    }

    public SmsaRecepientMaster buildTempToMaster(RecepientDTO entity) {
        SmsaRecepientMaster pojo = new SmsaRecepientMaster();
        try {
            pojo.setSmsaRamId(entity.getSmsaRamId());
            pojo.setRecEmpId(entity.getRecEmpId());
            pojo.setRecEmailId(entity.getRecEmailId());
            pojo.setRecEmpName(entity.getRecEmpName());
            pojo.setRecGeoName(entity.getRecGeoName());
            pojo.setRecSenderBic(entity.getRecSenderBic());
            pojo.setRecMsgType(entity.getRecMsgType());
            pojo.setRecGrade(entity.getRecGrade());
            pojo.setRecCreatedBy(entity.getRecCreatedBy());
            pojo.setRecCreatedDate(entity.getRecCreatedDate());
            pojo.setRecModifiedBy(entity.getRecModifiedBy());
            pojo.setRecModifiedDate(entity.getRecModifiedDate());
            pojo.setRecVerifiedBy(entity.getRecVerifiedBy());
            pojo.setRecVerifiedDate(entity.getRecVerifiedDate());
            pojo.setRecCategory(entity.getRecCategory());
            pojo.setRecCCEmpId(entity.getRecCCEmpId());
            pojo.setRecCCMailId(entity.getRecCCMailId());
            pojo.setSmsaRecStatus("Active");
        } catch (Exception e) {
            logger.error("Error mapping entity to DTO: {}", e.getMessage(), e);
        }
        return pojo;
    }

    public String approveRejectRecepientData(RecepientDTO recepientDTO, String action) {
        try {
            SmsaRecepientMaster srm = buildTempToMaster(recepientDTO);
            SmsaRecepientTemp srt = buildPojoToEntityCombo(recepientDTO);

            if (action.equalsIgnoreCase("Approved")) {
                if (recepientDTO.getSmsaRecOperation().equalsIgnoreCase("ADD") || recepientDTO.getSmsaRecOperation().equalsIgnoreCase("UPDATE")) {
                    recepientMasterRepo.save(srm);
                    recepientTempRepo.delete(srt);
                    logger.info("Recipient approved and moved to master: {}", recepientDTO);

                }
                if (recepientDTO.getSmsaRecOperation().equalsIgnoreCase("DELETE")) {
                    srm.setSmsaRecStatus("InActive");
                    recepientMasterRepo.save(srm);
                    recepientTempRepo.delete(srt);
                }
                return "Receipient " + recepientDTO.getSmsaRecOperation().toLowerCase() + " Operation Approved Successfully";

            } else {
                recepientTempRepo.delete(srt);
                logger.info("Recipient rejected and deleted from temp: {}", recepientDTO);
                return "Rejected Successfully";
            }
        } catch (Exception e) {
            logger.error("Error in approval/rejection process: {}", e.getMessage(), e);
            return "Failed to process approval/rejection due to internal error.";
        }
    }
}
