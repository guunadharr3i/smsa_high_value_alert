package com.smsa.highValueAlerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsa.highValueAlerts.entity.SmsaRecepientMaster;

@Repository
public interface RecepientMasterRepo extends JpaRepository<SmsaRecepientMaster, Long> {

    // Add custom methods if needed
   boolean existsByRecEmpIdAndRecGeoNameAndRecSenderBicAndRecMsgType(
    String recEmpId,
    String recGeoName,
    String recSenderBic,
    String recMsgType
);


}
