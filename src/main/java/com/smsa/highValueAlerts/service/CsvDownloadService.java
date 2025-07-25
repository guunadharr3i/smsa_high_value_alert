/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.RecepientDTO;
import com.smsa.highValueAlerts.DTO.RecepientFilterPojo;
import com.smsa.highValueAlerts.DTO.ThresholdDTO;
import com.smsa.highValueAlerts.DTO.ThresholdFilterPojo;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author abcom
 */
public class CsvDownloadService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CsvDownloadService.class);

    @Autowired
    private SmsaRecepientMasterService recepientMasterService;

    @Autowired
    private SmsaThresholdMasterService smsaThresholdMasterService;

    public byte[] exportRecepientMasterToCsv(RecepientFilterPojo filters) {
        log.info("Starting exportRecepientMasterToCsv CSV export...");

        List<RecepientDTO> headers = recepientMasterService.getFilteredMessages(filters);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            // Write Header Row
            writer.write(String.join(",", new String[]{
                "EmpId", "GeoName", "SenderBic", "MsgType", "EmpName",
                "Grade",
                "Created By", "Modified By", "Modified Date", "Verified By", "Verified Date"
            }));
            writer.write("\n");

            // Write Data Rows
            for (RecepientDTO h : headers) {
                String[] row = new String[]{
                    safe(h.getSmsaEmpId()),
                    safe(h.getSmsaGeoName()),
                    safe(h.getSmsaSenderBic()),
                    safe(h.getSmsaMsgType()),
                    safe(h.getSmsaEmpName()),
                    safe(h.getSmsaGrade()),
                    safe(h.getSmsaCreatedBy()),
                    safe(h.getSmsaModifiedBy()),
                    safe(h.getSmsaModifiedDate()),
                    safe(h.getSmsaVerifiedBy()),
                    safe(h.getSmsaVerifiedDate())
                };
                writer.write(String.join(",", escapeCsv(row)));
                writer.write("\n");
            }

            writer.flush();
            log.info("CSV generation completed with {} records.", headers.size());
            return out.toByteArray();

        } catch (Exception e) {
            log.error("CSV export failed", e);
            return new byte[0];
        }
    }

    public byte[] exportThresHoldMasterToCsv(ThresholdFilterPojo filters) {
        log.info("Starting exportRecepientMasterToCsv CSV export...");

        List<ThresholdDTO> headers = smsaThresholdMasterService.getFilteredMessages(filters);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            // Write Header Row
            writer.write(String.join(",", new String[]{
                "Threshold Id", "Msg Currency", "SenderBic", "MsgType", "Category A From Amount",
                "CategoryA ToAmount",
                "categoryB FromAmount", "CategoryB ToAmount", "createdBy", "createdDate", "Modified By", "Modified Date", "Verified By", "Verified Date"
            }));
            writer.write("\n");

            // Write Data Rows
            for (ThresholdDTO h : headers) {
                String[] row = new String[]{
                    safe(h.getThresholdId()),
                    safe(h.getMsgCurrency()),
                    safe(h.getSenderBic()),
                    safe(h.getMsgType()),
                    safe(h.getCategoryAFromAmount()),
                    safe(h.getCategoryAToAmount()),
                    safe(h.getCategoryBFromAmount()),
                    safe(h.getCategoryBToAmount()),
                    safe(h.getCreatedBy()),
                    safe(h.getCreatedDate()),
                    safe(h.getModifiedBy()),
                    safe(h.getModifiedDate()),
                    safe(h.getVerifiedBy()),
                    safe(h.getVerifiedDate())
                };
                writer.write(String.join(",", escapeCsv(row)));
                writer.write("\n");
            }

            writer.flush();
            log.info("CSV generation completed with {} records.", headers.size());
            return out.toByteArray();

        } catch (Exception e) {
            log.error("CSV export failed", e);
            return new byte[0];
        }
    }

    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private String[] escapeCsv(String[] values) {
        String[] escaped = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            String val = values[i];
            if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
                val = "\"" + val.replace("\"", "\"\"") + "\"";
            }
            escaped[i] = val;
        }
        return escaped;
    }
}
