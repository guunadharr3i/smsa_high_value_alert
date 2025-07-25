/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.DTO;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author abcom
 */
public class RecepientFilterPojo {

    private List<Long> smsaRamId;

    private List< String> smsaEmpId;

    private List< String> smsaGeoName;

    private List< String> smsaSenderBic;

    private List< Long> smsaMsgType;

    private List< String> smsaEmpName;

    private List< String> smsaGrade;

    private List< String> smsaCreatedBy;

    private List< String> smsaModifiedBy;

    private List< LocalDate> smsaModifiedDate;

    private List< String> smsaVerifiedBy;

    private List< LocalDate> smsaVerifiedDate;
}
