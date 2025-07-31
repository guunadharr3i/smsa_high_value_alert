package com.smsa.highValueAlerts.controller;

import com.smsa.highValueAlerts.DTO.*;
import com.smsa.highValueAlerts.entity.SmsaRecepientTemp;
import com.smsa.highValueAlerts.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class SmsaHighValueAlertsController {

    private static final Logger log = LogManager.getLogger(SmsaHighValueAlertsController.class);

    @Autowired
    SmsaRecepientTempService smsaRecepientTempService;

    @Autowired
    SmsaRecepientMasterService smsaRecepientMasterService;

    @Autowired
    SmsaThresholdTempService smsaThresholdTempService;

    @Autowired
    SmsaThresholdMasterService smsaThresholdMasterService;

    @PostMapping("/recipient/actions")
    public ResponseEntity<?> recipientData(@RequestBody ReciepientRequestDto reciepientRequestDto) {
        log.info("Received recipient operation: {}", reciepientRequestDto.getOperation());
        try {
            switch (reciepientRequestDto.getOperation().toUpperCase()) {
                case "ADD":
                    String addMsg = smsaRecepientTempService.addRecepientTempData(reciepientRequestDto.getRecepientDTO());
                    return ResponseEntity.ok(addMsg);
                case "UPDATE":
                    String updateMsg = smsaRecepientTempService.updateRecieptData(reciepientRequestDto.getRecepientDTO());
                    return ResponseEntity.ok(updateMsg);
                case "DELETE":
                    if (reciepientRequestDto.getRecepientDTO().getSmsaRamId() != null) {
                        String msg = smsaRecepientTempService.deleteRecepientByEmpId(reciepientRequestDto.getRecepientDTO().getSmsaRamId());
                        return ResponseEntity.ok(msg);
                    }
                    return ResponseEntity.ok("Id not found to delete");
                case "APPROVED":
                    String approved = smsaRecepientTempService.approveRejectRecepientData(reciepientRequestDto.getRecepientDTO(), "Approved");
                    return ResponseEntity.ok(approved);
                case "REJECTED":
                    String rejected = smsaRecepientTempService.approveRejectRecepientData(reciepientRequestDto.getRecepientDTO(), "Rejected");
                    return ResponseEntity.ok(rejected);
                default:
                    return ResponseEntity.badRequest().body("Invalid operationType: " + reciepientRequestDto.getOperation());
            }
        } catch (Exception e) {
            log.error("Error occurred while processing recipient data", e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/threshold/actions")
    public ResponseEntity<?> thresholdData(@RequestBody ThresholdRequestDTO thresholdRequestDTO) {
        log.info("Received threshold operation: {}", thresholdRequestDTO.getOperation());
        try {
            switch (thresholdRequestDTO.getOperation().toUpperCase()) {
                case "ADD":
                    String addMsg = smsaThresholdTempService.addThresholdTempData(thresholdRequestDTO.getThresholdDTO());
                    return ResponseEntity.ok(addMsg);
                case "UPDATE":
                    String updateMsg = smsaThresholdTempService.updateThresholdData(thresholdRequestDTO.getThresholdDTO());
                    return ResponseEntity.ok(updateMsg);
                case "DELETE":
                    if (thresholdRequestDTO.getThresholdDTO().getThresholdId() != null) {
                        smsaRecepientTempService.deleteRecepientByEmpId(thresholdRequestDTO.getThresholdDTO().getThresholdId());
                        return ResponseEntity.ok("Delete Request Went for approval");
                    }
                    return ResponseEntity.ok("Id not found to delete");
                case "APPROVED":
                    String approved = smsaThresholdTempService.approveRejectThresholdData(thresholdRequestDTO.getThresholdDTO(), "Approved");
                    return ResponseEntity.ok(approved);
                case "REJECTED":
                    String rejected = smsaThresholdTempService.approveRejectThresholdData(thresholdRequestDTO.getThresholdDTO(), "Rejected");
                    return ResponseEntity.ok(rejected);
                default:
                    return ResponseEntity.badRequest().body("Invalid operationType: " + thresholdRequestDTO.getOperation());
            }
        } catch (Exception e) {
            log.error("Error occurred while processing threshold data", e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/recepient/fetchRecepientTempData")
    public ResponseEntity<?> getRecepientTempData(@RequestBody Map<String, String> tokenMap) {
        try {
            String recEmpId = tokenMap.get("RecEmpId");
            List<RecepientDTO> recepientTempData = smsaRecepientTempService.getRecepientTempData(recEmpId);
            return ResponseEntity.ok(recepientTempData);
        } catch (Exception e) {
            log.error("Error while fetching recepient temp data", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/recepient/fetchRecepientMasterData")
    public ResponseEntity<?> getRecepientMasterData(@RequestBody Map<String, String> tokenMap) {
        try {
            List<RecepientDTO> recepientMasterData = smsaRecepientMasterService.getRecepientMasterData();
            return ResponseEntity.ok(recepientMasterData);
        } catch (Exception e) {
            log.error("Error while fetching recepient master data", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/threshold/fetchThresholdTempData")
    public ResponseEntity<?> getThresholdTempData(@RequestBody Map<String, String> tokenMap) {
        try {
            String recEmpId = tokenMap.get("RecEmpId");
            List<ThresholdDTO> thresholdTempData = smsaThresholdTempService.getThresholdTempData(recEmpId);
            return ResponseEntity.ok(thresholdTempData);
        } catch (Exception e) {
            log.error("Error while fetching threshold temp data", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/threshold/fetchThresholdMasterData")
    public ResponseEntity<?> getThresholdMasterData(@RequestBody Map<String, String> tokenMap) {
        try {
            List<ThresholdDTO> thresholdMasterData = smsaThresholdMasterService.getThresholdMasterData();
            return ResponseEntity.ok(thresholdMasterData);
        } catch (Exception e) {
            log.error("Error while fetching threshold master data", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/")
    public String getData() {
        return "High Value Alerts Deployed Successfully";
    }

    @GetMapping("/getFullTempData")
    public List<SmsaRecepientTemp> getTempData() {
        return smsaRecepientTempService.getFullTempData();
    }

    @PostMapping("/recipientSearch")
    public ResponseEntity<?> searchRecp(@RequestBody ReceipientSearchRequest receipientSearchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<RecepientDTO> data = smsaRecepientMasterService.getFilteredMessages(receipientSearchRequest.getRecepientFilterPojo(), pageable);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("Error occurred during recipient search", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/thresholdSearch")
    public ResponseEntity<?> searchThreshold(@RequestBody ThresholdSearchRequest thresholdSearchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ThresholdDTO> data = smsaThresholdMasterService.getFilteredMessages(thresholdSearchRequest.getThresholdFilterPojo(), pageable);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("Error occurred during threshold search", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

//    @PostMapping("/fetchUserData")
//    public ResponseEntity<?> fetchUserData(@RequestBody String empId) {
//        try {
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Internal Server Error");
//        }
//    }
}
