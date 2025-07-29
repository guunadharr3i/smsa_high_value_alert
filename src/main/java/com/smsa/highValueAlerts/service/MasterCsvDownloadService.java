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
import org.springframework.stereotype.Service;

/**
 *
 * @author abcom
 */
@Service
public class MasterCsvDownloadService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MasterCsvDownloadService.class);

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
                "SMSA_REC_EMP_ID", "SMSA_REC_EMAIL_ID", "SMSA_REC_EMP_NAME", "SMSA_REC_GEO_NAME", "SMSA_REC_SENDER_BIC",
                "SMSA_REC_MSG_TYPE","SMSA_REC_GRADE","SMSA_REC_CREATED_BY","SMSA_REC_CREATED_DATE","SMSA_REC_MODIFIED_BY",
                "SMSA_REC_MODIFIED_DATE", "SMSA_REC_VERIFIED_BY", "SMSA_REC_VERIFIED_DATE", "SMSA_REC_CATEGORY", "SMSA_REC_CC_EMPID",
                "SMSA_REC_CC_MAIL_ID","SMSA_REC_STATUS"
            }));
            writer.write("\n");

            // Write Data Rows
            for (RecepientDTO h : headers) {
                String[] row = new String[]{
                    safe(h.getRecEmpId()),
                    safe(h.getRecEmailId()),
                    safe(h.getRecEmpName()),
                    safe(h.getRecGeoName()),
                    safe(h.getRecSenderBic()),
                    safe(h.getRecMsgType()),
                    safe(h.getRecGrade()),
                    safe(h.getRecCreatedBy()),
                    safe(h.getRecCreatedDate()),
                    safe(h.getRecModifiedBy()),
                    safe(h.getRecModifiedDate()),
                    safe(h.getRecVerifiedBy()),
                    safe(h.getRecVerifiedDate()),
                    safe(h.getRecCategory()),
                    safe(h.getRecCCEmpId()),
                    safe(h.getRecCCMailId()),
                    safe(h.getSmsaRecStatus())
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
