package com.smsa.highValueAlerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsa.highValueAlerts.entity.SmsaRecepientTemp;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecepientTempRepo extends JpaRepository<SmsaRecepientTemp, Long> {
    // Custom query methods can be added here

    boolean existsByRecEmpIdAndRecGeoNameAndRecSenderBicAndRecMsgType(
            String recEmpId,
            String recGeoName,
            String recSenderBic,
            String recMsgType
    );

    Optional<SmsaRecepientTemp> findByRecEmpId(String recEmpId);

    void deleteBySmsaRamId(Long smsaRamId);

    List<SmsaRecepientTemp> findByRecCreatedByNot(String recCreatedBy);

}
