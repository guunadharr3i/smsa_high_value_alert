/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

/**
 *
 * @author abcom
 */
public class UseExistingOrGeneratedThresholdId extends SequenceStyleGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        try {
            Method getIdMethod = object.getClass().getMethod("getThresholdId");
            Object id = getIdMethod.invoke(object);
            if (id != null) {
                return (Serializable) id;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to access getThresholdId via reflection", e);
        }

        return super.generate(session, object);
    }
}
