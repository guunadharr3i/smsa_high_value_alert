package com.smsa.highValueAlerts.service;

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
            boolean existsInMs = recepientMasterRepo.existsBySmsaEmpIdAndSmsaGeoNameAndSmsaSenderBicAndSmsaMsgType(
                    recepientDTO.getSmsaEmpId(),
                    recepientDTO.getSmsaGeoName(),
                    recepientDTO.getSmsaSenderBic(),
                    recepientDTO.getSmsaMsgType()
            );

            if (existsInMs) {
                logger.warn("Recipient already exists in master table: {}", recepientDTO);
                return String.format(
                        "Recipient already exists in masterTable with EmpId = '%s', GeoName = '%s', SenderBIC = '%s', MsgType = '%s'.",
                        recepientDTO.getSmsaEmpId(),
                        recepientDTO.getSmsaGeoName(),
                        recepientDTO.getSmsaSenderBic(),
                        recepientDTO.getSmsaMsgType()
                );
            }

            boolean existsTemp = recepientTempRepo.existsBySmsaEmpIdAndSmsaGeoNameAndSmsaSenderBicAndSmsaMsgType(
                    recepientDTO.getSmsaEmpId(),
                    recepientDTO.getSmsaGeoName(),
                    recepientDTO.getSmsaSenderBic(),
                    recepientDTO.getSmsaMsgType()
            );

            if (existsTemp) {
                logger.warn("Recipient already exists in temp table: {}", recepientDTO);
                return String.format(
                        "Recipient data already waiting for approval with EmpId = '%s', GeoName = '%s', SenderBIC = '%s', MsgType = '%s'.",
                        recepientDTO.getSmsaEmpId(),
                        recepientDTO.getSmsaGeoName(),
                        recepientDTO.getSmsaSenderBic(),
                        recepientDTO.getSmsaMsgType()
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
                boolean existsInMs = recepientMasterRepo.existsBySmsaEmpIdAndSmsaGeoNameAndSmsaSenderBicAndSmsaMsgType(
                        recepientDTO.getSmsaEmpId(),
                        recepientDTO.getSmsaGeoName(),
                        recepientDTO.getSmsaSenderBic(),
                        recepientDTO.getSmsaMsgType()
                );
                boolean existsTemp = recepientTempRepo.existsBySmsaEmpIdAndSmsaGeoNameAndSmsaSenderBicAndSmsaMsgType(
                        recepientDTO.getSmsaEmpId(),
                        recepientDTO.getSmsaGeoName(),
                        recepientDTO.getSmsaSenderBic(),
                        recepientDTO.getSmsaMsgType()
                );

                if (existsInMs || existsTemp) {
                    SmsaRecepientTemp smsaRecepientTemp = buildPojoToEntityCombo(recepientDTO);
                    recepientTempRepo.save(smsaRecepientTemp);
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

    public String deleteRecepientByEmpId(Long smsaRamId) {
        try {
            if (smsaRamId == null) {
                logger.warn("Attempted to delete with null smsaRamId");
                return "smsaRamId must not be null";
            }

            Optional<SmsaRecepientMaster> existing = recepientMasterRepo.findById(smsaRamId);
            if (existing.isPresent()) {
                recepientTempRepo.deleteBySmsaRamId(smsaRamId);
                logger.info("Recipient deleted with smsaRamId: {}", smsaRamId);
                return "Recipient deleted successfully";
            } else {
                logger.warn("Recipient with smsaRamId {} not found", smsaRamId);
                return "Recipient with smsaRamId " + smsaRamId + " not found";
            }
        } catch (Exception e) {
            logger.error("Error deleting recipient: {}", e.getMessage(), e);
            return "Failed to delete recipient due to internal error.";
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
        pojo.setSmsaRamId(entity.getSmsaRamId());
        pojo.setSmsaEmpId(entity.getSmsaEmpId());
        pojo.setSmsaGeoName(entity.getSmsaGeoName());
        pojo.setSmsaSenderBic(entity.getSmsaSenderBic());
        pojo.setSmsaMsgType(entity.getSmsaMsgType());
        pojo.setSmsaEmpName(entity.getSmsaEmpName());
        pojo.setSmsaGrade(entity.getSmsaGrade());
        pojo.setSmsaCreatedBy(entity.getSmsaCreatedBy());
        pojo.setSmsaModifiedBy(entity.getSmsaModifiedBy());
        pojo.setSmsaModifiedDate(entity.getSmsaModifiedDate());
        pojo.setSmsaVerifiedBy(entity.getSmsaVerifiedBy());
        pojo.setSmsaVerifiedDate(entity.getSmsaVerifiedDate());
        pojo.setSmsaAction(entity.getSmsaAction());
        return pojo;
    }

    public SmsaRecepientTemp buildPojoToEntityCombo(RecepientDTO recepientDTO) {
        SmsaRecepientTemp smsaRecepientTemp = new SmsaRecepientTemp();
        smsaRecepientTemp.setSmsaRamId(recepientDTO.getSmsaRamId());
        smsaRecepientTemp.setSmsaEmpId(recepientDTO.getSmsaEmpId());
        smsaRecepientTemp.setSmsaGeoName(recepientDTO.getSmsaGeoName());
        smsaRecepientTemp.setSmsaSenderBic(recepientDTO.getSmsaSenderBic());
        smsaRecepientTemp.setSmsaMsgType(recepientDTO.getSmsaMsgType());
        smsaRecepientTemp.setSmsaEmpName(recepientDTO.getSmsaEmpName());
        smsaRecepientTemp.setSmsaGrade(recepientDTO.getSmsaGrade());
        smsaRecepientTemp.setSmsaCreatedBy(recepientDTO.getSmsaCreatedBy());
        smsaRecepientTemp.setSmsaModifiedBy(recepientDTO.getSmsaModifiedBy());
        smsaRecepientTemp.setSmsaModifiedDate(recepientDTO.getSmsaModifiedDate());
        smsaRecepientTemp.setSmsaVerifiedBy(recepientDTO.getSmsaVerifiedBy());
        smsaRecepientTemp.setSmsaVerifiedDate(recepientDTO.getSmsaVerifiedDate());
        smsaRecepientTemp.setSmsaAction(recepientDTO.getSmsaAction());
        return smsaRecepientTemp;
    }

    public SmsaRecepientMaster buildTempToMaster(RecepientDTO recepientDTO) {
        SmsaRecepientMaster smsaRecepientTemp = new SmsaRecepientMaster();
        smsaRecepientTemp.setSmsaRamId(recepientDTO.getSmsaRamId());
        smsaRecepientTemp.setSmsaEmpId(recepientDTO.getSmsaEmpId());
        smsaRecepientTemp.setSmsaGeoName(recepientDTO.getSmsaGeoName());
        smsaRecepientTemp.setSmsaSenderBic(recepientDTO.getSmsaSenderBic());
        smsaRecepientTemp.setSmsaMsgType(recepientDTO.getSmsaMsgType());
        smsaRecepientTemp.setSmsaEmpName(recepientDTO.getSmsaEmpName());
        smsaRecepientTemp.setSmsaGrade(recepientDTO.getSmsaGrade());
        smsaRecepientTemp.setSmsaCreatedBy(recepientDTO.getSmsaCreatedBy());
        smsaRecepientTemp.setSmsaModifiedBy(recepientDTO.getSmsaModifiedBy());
        smsaRecepientTemp.setSmsaModifiedDate(recepientDTO.getSmsaModifiedDate());
        smsaRecepientTemp.setSmsaVerifiedBy(recepientDTO.getSmsaVerifiedBy());
        smsaRecepientTemp.setSmsaVerifiedDate(recepientDTO.getSmsaVerifiedDate());
        smsaRecepientTemp.setSmsaStatus("Active");
        return smsaRecepientTemp;
    }

    public String approveRejectRecepientData(RecepientDTO recepientDTO, String action) {
        try {
            SmsaRecepientMaster srm = buildTempToMaster(recepientDTO);
            SmsaRecepientTemp srt = buildPojoToEntityCombo(recepientDTO);

            if (action.equalsIgnoreCase("Approved")) {
                recepientMasterRepo.save(srm);
                recepientTempRepo.delete(srt);
                logger.info("Recipient approved and moved to master: {}", recepientDTO);
                return "Saved Successfully in master";
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
