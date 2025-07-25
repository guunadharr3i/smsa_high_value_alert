package com.smsa.highValueAlerts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smsa.highValueAlerts.DTO.RecepientDTO;
import com.smsa.highValueAlerts.entity.SmsaRecepientMaster;
import com.smsa.highValueAlerts.entity.SmsaRecepientTemp;
import com.smsa.highValueAlerts.repository.RecepientMasterRepo;
import com.smsa.highValueAlerts.repository.RecepientTempRepo;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SmsaRecepientTempService {

    @Autowired
    private RecepientTempRepo recepientTempRepo;

    @Autowired
    private RecepientMasterRepo recepientMasterRepo;

    public String addRecepientTempData(RecepientDTO recepientDTO) {

        // Check uniqueness of the 4-column combination
        boolean existsInMs = recepientMasterRepo.existsBySmsaEmpIdAndSmsaGeoNameAndSmsaSenderBicAndSmsaMsgType(
                recepientDTO.getSmsaEmpId(),
                recepientDTO.getSmsaGeoName(),
                recepientDTO.getSmsaSenderBic(),
                recepientDTO.getSmsaMsgType()
        );

        if (existsInMs) {
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
            return String.format(
                    "Recipient data already waiting for approval with EmpId = '%s', GeoName = '%s', SenderBIC = '%s', MsgType = '%s'.",
                    recepientDTO.getSmsaEmpId(),
                    recepientDTO.getSmsaGeoName(),
                    recepientDTO.getSmsaSenderBic(),
                    recepientDTO.getSmsaMsgType()
            );
        }

        // Proceed with insert
        SmsaRecepientTemp smsaRecepientTemp = buildPojoToEntityCombo(recepientDTO);
        recepientTempRepo.save(smsaRecepientTemp);

        return "Recipient added successfully and went for approval";
    }

    public String updateRecieptData(RecepientDTO recepientDTO) {
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
                return "Updated Successfully ,Went for approval";
            }
        }
        return "Id not found to update";
    }

    public String deleteRecepientByEmpId(Long smsaRamId) {
        if (smsaRamId == null) {
            return "smsaRamId must not be null";
        }

        Optional<SmsaRecepientMaster> existing = recepientMasterRepo.findById(smsaRamId);
        if (existing.isPresent()) {
            recepientTempRepo.deleteBySmsaRamId(smsaRamId);
            return "Recipient deleted successfully";
        } else {
            return "Recipient with smsaRamId " + smsaRamId + " not found";
        }
    }

    public List<RecepientDTO> getRecepientTempData() {
        List<SmsaRecepientTemp> data = recepientTempRepo.findAll();
        List<RecepientDTO> pojoList = data.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());
        return pojoList;
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

        smsaRecepientTemp.setSmsaRamId(recepientDTO.getSmsaRamId() != null ? recepientDTO.getSmsaRamId() : null);
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

        smsaRecepientTemp.setSmsaRamId(recepientDTO.getSmsaRamId() != null ? recepientDTO.getSmsaRamId() : null);
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
        if (action.equalsIgnoreCase("Approved")) {
            SmsaRecepientMaster srm = buildTempToMaster(recepientDTO);
            recepientMasterRepo.save(srm);
            return "Saved Successfully in master";

        }
        return "Rejected Successfully";
    }

}
