/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.SmsaAccountPojo;
import com.smsa.highValueAlerts.DTO.SwiftAccountSearchRequestPojo;
import com.smsa.highValueAlerts.entity.SmsaAccountTemp;
import com.smsa.highValueAlerts.repository.SmsaAccountTempRepo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author abcom
 */
@Service
public class SmsaTempAccountService {
    
    private static final Logger logger = LogManager.getLogger(SmsaMasterAccountService.class);
    
    @Autowired
    private SmsaAccountTempRepo smsaAccountTempRepo;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Page<SmsaAccountPojo> searchMasterAccountData(SwiftAccountSearchRequestPojo acc, Pageable pageable) {
        List<SmsaAccountTemp> resultList = new ArrayList<>();
        long totalCount = 0;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SmsaAccountTemp> query = cb.createQuery(SmsaAccountTemp.class);
            Root<SmsaAccountTemp> root = query.from(SmsaAccountTemp.class);
            List<Predicate> predicates = buildDynamicPredicates(acc, cb, root);
            query.select(root).distinct(true);
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            List<Order> orderOfSorting = new ArrayList<>();
            
            orderOfSorting.add(cb.desc(root.get("accountNO")));
            
            query.orderBy(orderOfSorting);
            
            TypedQuery<SmsaAccountTemp> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
            resultList = typedQuery.getResultList();
            
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SmsaAccountTemp> countRoot = countQuery.from(SmsaAccountTemp.class);
            List<Predicate> countPredicates = buildDynamicPredicates(acc, cb, countRoot);
            
            countQuery.select(cb.countDistinct(countRoot));
            if (!countPredicates.isEmpty()) {
                countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
            }
            
            totalCount = entityManager.createQuery(countQuery).getSingleResult();
            
        } catch (Exception e) {
            logger.error("Exception occurred while filtering Swift messages: {}", e.getMessage(), e);
        }
        
        List<SmsaAccountPojo> pojoList = resultList.stream()
                .map(this::updtePojoFromMaster)
                .collect(Collectors.toList());
        
        return new PageImpl<>(pojoList, pageable, totalCount);
    }

    private SmsaAccountPojo updtePojoFromMaster(SmsaAccountTemp temp) {
        SmsaAccountPojo master = new SmsaAccountPojo();
        master.setId(temp.getId());
        master.setIo(temp.getIo());
        master.setSenderBIC(temp.getSenderBIC());
        master.setLocation(temp.getLocation());
        master.setMessageTyp(temp.getMessageTyp());
        master.setReceivedBIC(temp.getReceivedBIC());
        master.setCurrency(temp.getCurrency());
        master.setTeam(temp.getTeam());
        master.setRemark(temp.getRemark());
        master.setBankName(temp.getBankName());
        master.setCrudOperation(temp.getActionType());
        master.setAccStatus(temp.getStatus());
        return master;
    }

    private List<Predicate> buildDynamicPredicates(SwiftAccountSearchRequestPojo filter, CriteriaBuilder cb, Root<SmsaAccountTemp> root) {
        List<Predicate> predicates = new ArrayList<>();
        
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(SwiftAccountSearchRequestPojo.class).getPropertyDescriptors()) {
                String fieldName = pd.getName();
                Object value;
                
                if (!"class".equals(fieldName) && !"sortType".equals(fieldName) && !"columnSort".equals(fieldName)) {
                    value = pd.getReadMethod().invoke(filter);
                    if (value != null) {
                        if (value instanceof List) {
                            List<?> rawList = (List<?>) value;

                            // Remove nulls and empty strings with only spaces
                            List<?> filteredList = rawList.stream()
                                    .filter(Objects::nonNull)
                                    .filter(item -> !(item instanceof String) || !((String) item).trim().isEmpty())
                                    .collect(Collectors.toList());
                            
                            if (!filteredList.isEmpty()) {
                                Predicate predicate = buildPredicateForField(fieldName, filteredList, cb, root);
                                if (predicate != null) {
                                    predicates.add(predicate);
                                }
                            }
                        } else {
                            if (value instanceof String) {
                                String val = (String) value;
                                if (!val.trim().isEmpty()) {
                                    Predicate predicate = buildPredicateForField(fieldName, value, cb, root);
                                    if (predicate != null) {
                                        predicates.add(predicate);
                                    }
                                }
                            } else {
                                Predicate predicate = buildPredicateForField(fieldName, value, cb, root);
                                if (predicate != null) {
                                    predicates.add(predicate);
                                }
                            }
                        }
                        
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error building dynamic predicates", e);
        }
        
        return predicates;
    }
    
    private Predicate buildPredicateForField(String fieldName, Object value, CriteriaBuilder cb, Root<SmsaAccountTemp> root) {
        if (value instanceof List && value != null && !((List<?>) value).isEmpty()) {
            return handleListPredicate(fieldName, (List<?>) value, cb, root);
        }
        
        if (value instanceof String) {
            String str = ((String) value).trim();
            if (!str.isEmpty()) {
                return cb.like(cb.lower(root.get(fieldName)), "%" + escapeLike(str.toLowerCase()) + "%");
            }
            return null;
        }
        
        return cb.equal(root.get(fieldName), value);
    }
    
    private Predicate handleListPredicate(String fieldName, List<?> list, CriteriaBuilder cb, Root<SmsaAccountTemp> root) {
        if (list.isEmpty()) {
            return null;
        }
        
        List<Predicate> likePredicates = new ArrayList<>();
        
        if (list.get(0) instanceof String) {
            for (Object item : list) {
                if (item != null) {
                    // Convert column to string using TO_CHAR
                    Expression<String> fieldAsString = cb.function("TO_CHAR", String.class, root.get(fieldName));
                    likePredicates.add(
                            cb.like(cb.lower(fieldAsString), "%" + escapeLike(item.toString().toLowerCase()) + "%")
                    );
                }
            }
            return cb.or(likePredicates.toArray(new Predicate[0]));
        }
        
        return root.get(fieldName).in(list);
    }
    
    private String escapeLike(String param) {
        return param.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("%", "\\%");
    }
    
}
