/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.controller;

import com.smsa.highValueAlerts.DTO.AccountSearchResponse;
import com.smsa.highValueAlerts.DTO.SmsaAccountPojo;
import com.smsa.highValueAlerts.DTO.SmsaAccountReq;
import com.smsa.highValueAlerts.DTO.SwiftAccountSearchRequestPojo;
import com.smsa.highValueAlerts.service.SmsaMasterAccountService;
import com.smsa.highValueAlerts.service.SmsaTempAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author abcom
 */
@RequestMapping("/account")
@RestController
public class SmsaAccountAddController {

    @Autowired
    SmsaMasterAccountService smsaAccountService;
    
    @Autowired
    SmsaTempAccountService smsaTempAccountService;

    @PostMapping("/swiftAccoutOperations")
    public ResponseEntity<?> handleAccount(@RequestBody SmsaAccountReq request) {
        try {
            String result = smsaAccountService.processAccountOperations(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/searchMasterAccounts")
    public ResponseEntity<?> searchMasterAccounts(@RequestBody SwiftAccountSearchRequestPojo swiftAccountSearchRequestPojo, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SmsaAccountPojo> pageResult = smsaAccountService.searchMasterAccountData(swiftAccountSearchRequestPojo, pageable);
            AccountSearchResponse res = new AccountSearchResponse();
            res.setMessages(pageResult.getContent());
            res.setTotalElements(pageResult.getTotalElements());
            res.setTotalPages(pageResult.getTotalPages());
            res.setCurrentPage(pageResult.getNumber());
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception Occured please contact Team");
        }
    }
    
     @PostMapping("/searchTempAccounts")
    public ResponseEntity<?> searchTempAccounts(@RequestBody SwiftAccountSearchRequestPojo swiftAccountSearchRequestPojo, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SmsaAccountPojo> pageResult = smsaTempAccountService.searchMasterAccountData(swiftAccountSearchRequestPojo, pageable);
            AccountSearchResponse res = new AccountSearchResponse();
            res.setMessages(pageResult.getContent());
            res.setTotalElements(pageResult.getTotalElements());
            res.setTotalPages(pageResult.getTotalPages());
            res.setCurrentPage(pageResult.getNumber());
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception Occured please contact Team");
        }
    }

}
