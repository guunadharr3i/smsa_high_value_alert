/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smsa.highValueAlerts.DTO.FilterReceipientRequest;
import com.smsa.highValueAlerts.utils.AESUtil;
import com.smsa.highValueAlerts.utils.ApiResponseCode;
import com.smsa.highValueAlerts.utils.AuthenticateRequest;
import com.smsa.highValueAlerts.utils.DownloadApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author abcom
 */
public class SmsaHighValueDownloadController {

    private static final Logger logger = LogManager.getLogger(SmsaHighValueDownloadController.class);
    
     @Autowired
    private AuthenticateRequest authenticateApi;
    @Value("${aes.auth.key}")
    private String secretKey;
    @Value("${aes.auth.vi.key}")
    private String viKey;

    @PostMapping("/download")
    public ResponseEntity<?> getFilteredMessages(
            @RequestBody String encryptedRequest,
            @RequestParam String downloadType) {
        logger.info("Inside GetFIltered messages method");
        logger.info("Selected downloadType: " + downloadType);

        try {
            // Step 1: Decrypt incoming payload
            String decryptedJson = AESUtil.decrypt(encryptedRequest, secretKey, viKey);
            logger.info("DecryptedJson: " + decryptedJson);
            // Step 2: Convert decrypted JSON to FilterRequest
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            FilterReceipientRequest filter = mapper.readValue(decryptedJson, FilterReceipientRequest.class);
            // Step 3: Authentication
            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenMap());
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(DownloadApiResponse.error(ApiResponseCode.INVALID_TOKEN));
            }
            switch (downloadType.toUpperCase()) {
//                case "CCSV":
//                    return selectedDataToCsv(filter.getFilter());
//                case "CXLSX":
//                    return selectedSwiftHeadersToExcel(filter.getFilter());
                default:
                    return ResponseEntity.badRequest()
                            .body(DownloadApiResponse.error(ApiResponseCode.INVALID_DOWNLOAD_TYPE));
            }

        } catch (Exception e) {
            logger.error("Exception while processing download request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

}
