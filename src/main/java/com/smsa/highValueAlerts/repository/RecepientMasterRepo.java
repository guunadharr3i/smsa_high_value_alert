package com.smsa.highValueAlerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsa.highValueAlerts.entity.SmsaRecepientMaster;
import com.smsa.highValueAlerts.entity.SmsaRecepientTemp;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecepientMasterRepo extends JpaRepository<SmsaRecepientMaster, Long> {

    // Add custom methods if needed
    boolean existsByRecEmpIdAndRecGeoNameAndRecSenderBicAndRecMsgType(
            String recEmpId,
            String recGeoName,
            String recSenderBic,
            String recMsgType
    );

    Optional<SmsaRecepientTemp> findByRecEmpIdAndRecGeoNameAndRecSenderBicAndRecMsgType(
            String recEmpId,
            String recGeoName,
            String recSenderBic,
            String recMsgType);
    Optional<SmsaRecepientMaster> findById(Long smsaRamId);

    List<SmsaRecepientMaster> findBySmsaRecStatus(String smsaRecStatus);


}
