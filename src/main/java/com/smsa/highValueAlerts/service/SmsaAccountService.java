package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.SmsaAccountReq;
import com.smsa.highValueAlerts.repository.SmsaAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsaAccountService {

    @Autowired
    private SmsaAccountRepository repository;

    public String processAccount(SmsaAccountReq request) {
        String action = request.getActionType();

        switch (action.toUpperCase()) {
            case "ADD":

                return "Account added successfully";

            case "EDIT":

                return "Account updated successfully";

            case "DELETE":

            default:
                throw new IllegalArgumentException("Invalid actionType: " + action);
        }
    }

}
