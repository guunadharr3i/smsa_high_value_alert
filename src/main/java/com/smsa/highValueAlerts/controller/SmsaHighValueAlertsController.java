package com.smsa.highValueAlerts.controller;

import com.smsa.highValueAlerts.DTO.RecepientDTO;
import com.smsa.highValueAlerts.DTO.ReciepientRequestDto;
import com.smsa.highValueAlerts.DTO.ThresholdDTO;
import com.smsa.highValueAlerts.service.SmsaRecepientMasterService;
import com.smsa.highValueAlerts.service.SmsaRecepientTempService;
import com.smsa.highValueAlerts.service.SmsaThresholdMasterService;
import com.smsa.highValueAlerts.service.SmsaThresholdTempService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsaHighValueAlertsController {

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
        switch (reciepientRequestDto.getOperation().toUpperCase()) {

            case "ADD":
                String addMsg = smsaRecepientTempService.addRecepientTempData(reciepientRequestDto.getRecepientDTO());
                return ResponseEntity.ok(addMsg);
            case "UPDATE":
                String updateMsg=smsaRecepientTempService.updateRecieptData(reciepientRequestDto.getRecepientDTO());

                return ResponseEntity.ok(updateMsg);
            case "DELETE":
                if (reciepientRequestDto.getRecepientDTO().getSmsaRamId() != null) {
                    smsaRecepientTempService.deleteRecepientByEmpId(reciepientRequestDto.getRecepientDTO().getSmsaRamId());
                    return ResponseEntity.ok("Delete Request Went for approval");
                }
                return ResponseEntity.ok("Id not found to delete");
            default:
                return ResponseEntity.badRequest().body("Invalid operationType: " + reciepientRequestDto.getOperation());

        }
    }

    @PostMapping("/recepient/fetchRecepientTempData")
    public ResponseEntity<?> getRecepientTempData(@RequestBody Map<String, String> tokenMap) {
        
        List<RecepientDTO> recepientTempData= smsaRecepientTempService.getRecepientTempData();
        
        return ResponseEntity.ok(recepientTempData);
    }
    
    @PostMapping("/recepient/fetchRecepientMasterData")
    public ResponseEntity<?> getRecepientMasterData(@RequestBody Map<String, String> tokenMap) {
        
        List<RecepientDTO> recepientMasterData= smsaRecepientMasterService.getRecepientMasterData();
        
        return ResponseEntity.ok(recepientMasterData);
    }
    
    @PostMapping("/threshold/fetchThresholdTempData")
    public ResponseEntity<?> getThresholdTempData(@RequestBody Map<String, String> tokenMap) {
        
        List<ThresholdDTO> thresholdTempData= smsaThresholdTempService.getThresholdTempData();
        
        return ResponseEntity.ok(thresholdTempData);
    }
    
    @PostMapping("/threshold/fetchThresholdMasterData")
    public ResponseEntity<?> getThresholdMasterData(@RequestBody Map<String, String> tokenMap) {
        
        List<ThresholdDTO> thresholdMasterData= smsaThresholdMasterService.getThresholdMasterData();
        
        return ResponseEntity.ok(thresholdMasterData);
    }

    @GetMapping("/")
    String getData() {
        return "High Value Alerts Deployed Successfully";
    }

}
