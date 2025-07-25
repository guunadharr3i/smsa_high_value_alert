/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.RecepientDTO;
import com.smsa.highValueAlerts.DTO.RecepientFilterPojo;
import com.smsa.highValueAlerts.DTO.ThresholdDTO;
import com.smsa.highValueAlerts.DTO.ThresholdFilterPojo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author abcom
 */
public class MasterXlsDownloadService {
    private static final Logger log = LogManager.getLogger(MasterXlsDownloadService.class);
    
    @Autowired
    SmsaRecepientMasterService smsaRecepientMasterService;

    @Autowired
    private SmsaThresholdMasterService smsaThresholdMasterService;

    public byte[] exportSwiftHeadersToSingleExcel(RecepientFilterPojo filters) throws IOException {
        log.info("Starting SwiftMessageHeader single Excel export...");

        List<RecepientDTO> headers = smsaRecepientMasterService.getFilteredMessages(filters);
        if (headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Returning empty Excel.");
        }

        // --- HSSFWorkbook instead of XSSFWorkbook ---
        try (Workbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Swift Headers");
            createHeaderRow(sheet);

            int rowNum = 1;
            for (RecepientDTO h : headers) {
                Row row = sheet.createRow(rowNum++);
                populateSheetRow(row, h);
            }

            // Adjust column widths (you can trim 34 if you have fewer columns)
            for (int col = 0; col < 34; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(out);
            log.info("Excel generation completed with {} records.", headers.size());
            return out.toByteArray();
        }
    }

    // ---------- helpers -----------------------------------------------------
    private void createHeaderRow(Sheet sheet) {
        String[] headers = {
            "EmpId", "GeoName", "SenderBic", "MsgType", "EmpName",
                "Grade",
                "Created By", "Modified By", "Modified Date", "Verified By", "Verified Date"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void populateSheetRow(Row row, RecepientDTO h) {
        row.createCell(0).setCellValue(safe(h.getSmsaEmpId()));
        row.createCell(1).setCellValue(safe(h.getSmsaGeoName()));
        row.createCell(2).setCellValue(safe(h.getSmsaSenderBic()));
        row.createCell(3).setCellValue(safe(h.getSmsaMsgType()));
        row.createCell(4).setCellValue(safe(h.getSmsaEmpName()));
        row.createCell(6).setCellValue(safe(h.getSmsaGrade()));
        row.createCell(7).setCellValue(safe(h.getSmsaCreatedBy()));
        row.createCell(8).setCellValue(safe(h.getSmsaModifiedBy()));
        row.createCell(9).setCellValue(safe(h.getSmsaModifiedDate()));
        row.createCell(10).setCellValue(safe(h.getSmsaVerifiedBy()));
        row.createCell(11).setCellValue(safe(h.getSmsaVerifiedDate()));
    }

   //  download XL for Threshold Master Data
    public byte[] exportThresholdDataSingleExcel(ThresholdFilterPojo filters) throws IOException {
        log.info("Starting SwiftThresholdMessageHeader single Excel export...");

        List<ThresholdDTO> headers = smsaThresholdMasterService.getFilteredMessages(filters);
        if (headers.isEmpty()) {
            log.warn("No SwiftThresholdMessageHeader records found. Returning empty Excel.");
        }

        // --- HSSFWorkbook instead of XSSFWorkbook ---
        try (Workbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Swift Headers");
            createThresholdHeaderRow(sheet);

            int rowNum = 1;
            for (ThresholdDTO h : headers) {
                Row row = sheet.createRow(rowNum++);
                populateThresholdSheetRow(row, h);
            }

            // Adjust column widths (you can trim 34 if you have fewer columns)
            for (int col = 0; col < 34; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(out);
            log.info("Excel Threshold generation completed with {} records.", headers.size());
            return out.toByteArray();
        }
    }

    // ---------- helpers -----------------------------------------------------
    private void createThresholdHeaderRow(Sheet sheet) {
        String[] headers = {
            "Threshold Id", "Msg Currency", "SenderBic", "MsgType", "Category A From Amount",
                "Category A To Amount",
                "category B From Amount", "Category B To Amount", "createdBy", "createdDate", "Modified By", "Modified Date", "Verified By", "Verified Date"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void populateThresholdSheetRow(Row row, ThresholdDTO h) {
        row.createCell(0).setCellValue(safe(h.getThresholdId()));
        row.createCell(1).setCellValue(safe(h.getMsgCurrency()));
        row.createCell(2).setCellValue(safe(h.getSenderBic()));
        row.createCell(3).setCellValue(safe(h.getMsgType()));
        row.createCell(4).setCellValue(safe(h.getCategoryAFromAmount()));
        row.createCell(6).setCellValue(safe(h.getCategoryAToAmount()));
        row.createCell(7).setCellValue(safe(h.getCategoryBFromAmount()));
        row.createCell(8).setCellValue(safe(h.getCategoryBToAmount()));
        row.createCell(9).setCellValue(safe(h.getCreatedBy()));
        row.createCell(10).setCellValue(safe(h.getCreatedDate()));
        row.createCell(11).setCellValue(safe(h.getModifiedBy()));
        row.createCell(11).setCellValue(safe(h.getModifiedDate()));
    }

    // ---------- utility -----------------------------------------------------
    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private String safeInt(Integer i) {
        return i == null ? "" : i.toString();
    }

    private String safeLong(Long l) {
        return l == null ? "" : l.toString();
    }
}
