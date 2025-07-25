/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smsa.highValueAlerts.DTO.FilterReceipientRequest;
import com.smsa.highValueAlerts.DTO.FilterThresholdRequest;
import com.smsa.highValueAlerts.DTO.RecepientFilterPojo;
import com.smsa.highValueAlerts.DTO.ThresholdFilterPojo;
import com.smsa.highValueAlerts.service.MasterCsvDownloadService;
import com.smsa.highValueAlerts.service.MasterXlsDownloadService;
import com.smsa.highValueAlerts.service.TempCsvDownloadService;
import com.smsa.highValueAlerts.service.TempXlsDownloadService;
import com.smsa.highValueAlerts.utils.AESUtil;
import com.smsa.highValueAlerts.utils.ApiResponseCode;
import com.smsa.highValueAlerts.utils.AuthenticateRequest;
import com.smsa.highValueAlerts.utils.DownloadApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RestController
@RequestMapping("/download")
public class SmsaHighValueDownloadController {

    private static final Logger logger = LogManager.getLogger(SmsaHighValueDownloadController.class);

    @Autowired
    private AuthenticateRequest authenticateApi;
    @Value("${aes.auth.key}")
    private String secretKey;
    @Value("${aes.auth.vi.key}")
    private String viKey;

    @Autowired
    TempCsvDownloadService tempCsvDownloadService;

    @Autowired
    TempXlsDownloadService tempXlsDownloadService;

    @Autowired
    MasterCsvDownloadService masterCsvDownloadService;

    @Autowired
    MasterXlsDownloadService masterXlsDownloadService;

    @PostMapping("/recepientDownload")
    public ResponseEntity<?> getReceipeientDownload(
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
//            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenMap());
//            if (accessToken == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(DownloadApiResponse.error(ApiResponseCode.INVALID_TOKEN));
//            }
            switch (downloadType.toUpperCase()) {
                case "RTCSV":
                    return selectedRecepientTempDataToCsv();
                case "RTXLS":
                    return exportRecepientTempToSingleExcel();
                case "RMCSV":
                    return selectedRecepientMasterDataToCsv(filter.getFilter());
                case "RMXLS":
                    return selectedRecepientMasterToExcel(filter.getFilter());
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

    @PostMapping("/thresholdDownload")
    public ResponseEntity<?> getThresholdDownload(
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
            FilterThresholdRequest filter = mapper.readValue(decryptedJson, FilterThresholdRequest.class);
            // Step 3: Authentication
            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenMap());
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(DownloadApiResponse.error(ApiResponseCode.INVALID_TOKEN));
            }
            switch (downloadType.toUpperCase()) {
                case "TTCSV":
                    return selectedThresholdTempDataToCsv();
                case "TTXLS":
                    return tempThresholdXlsDownloadService();
                case "TMCSV":
                    return selectedThresholdMasterDataToCsv(filter.getFilter());
                case "TMXLS":
                    return exportThresholdMasterToSingleExcel(filter.getFilter());
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

    public ResponseEntity<?> selectedRecepientMasterDataToCsv(RecepientFilterPojo filters) {
        logger.info("Exporting data to CSV file");

        try {
            byte[] csvData = masterCsvDownloadService.exportRecepientMasterToCsv(filters);

            if (csvData.length == 0) {
                logger.warn("Generated CSV file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
        } catch (Exception e) {
            logger.error("Failed to export CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> selectedRecepientTempDataToCsv() {
        logger.info("Exporting data to CSV file");

        try {
            byte[] csvData = tempCsvDownloadService.exportRecepientMasterToCsv();

            if (csvData.length == 0) {
                logger.warn("Generated CSV file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
        } catch (Exception e) {
            logger.error("Failed to export CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> selectedThresholdMasterDataToCsv(ThresholdFilterPojo filters) {
        logger.info("Exporting data to CSV file");

        try {
            byte[] csvData = masterCsvDownloadService.exportThresHoldMasterToCsv(filters);

            if (csvData.length == 0) {
                logger.warn("Generated CSV file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
        } catch (Exception e) {
            logger.error("Failed to export CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> selectedThresholdTempDataToCsv() {
        logger.info("Exporting data to CSV file");

        try {
            byte[] csvData = tempCsvDownloadService.exportThresHoldTempToCsv();

            if (csvData.length == 0) {
                logger.warn("Generated CSV file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
        } catch (Exception e) {
            logger.error("Failed to export CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> selectedRecepientMasterToExcel(RecepientFilterPojo filters) {
        logger.info("Exporting data to single Excel file");
        try {
            byte[] excelData = masterXlsDownloadService.exportSwiftHeadersToSingleExcel(filters);

            if (excelData == null || excelData.length == 0) {
                logger.warn("Generated Excel file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.xls");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            logger.error("Failed to export Excel file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> exportRecepientTempToSingleExcel() {
        logger.info("Exporting data to single Excel file");
        try {
            byte[] excelData = tempXlsDownloadService.exportRecepientTempToSingleExcel();

            if (excelData == null || excelData.length == 0) {
                logger.warn("Generated Excel file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.xls");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            logger.error("Failed to export Excel file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> exportThresholdMasterToSingleExcel(ThresholdFilterPojo filters) {
        logger.info("Exporting data to single Excel file");
        try {
            byte[] excelData = masterXlsDownloadService.exportThresholdDataSingleExcel(filters);

            if (excelData == null || excelData.length == 0) {
                logger.warn("Generated Excel file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.xls");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            logger.error("Failed to export Excel file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> tempThresholdXlsDownloadService() {
        logger.info("Exporting data to single Excel file");
        try {
            byte[] excelData = tempXlsDownloadService.exportThresholdDataSingleExcel();

            if (excelData == null || excelData.length == 0) {
                logger.warn("Generated Excel file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.xls");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            logger.error("Failed to export Excel file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

}
