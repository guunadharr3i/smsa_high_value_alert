package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.ThresholdDTO;
import com.smsa.highValueAlerts.DTO.ThresholdFilterPojo;
import com.smsa.highValueAlerts.entity.SmsaThresholdMaster;
import com.smsa.highValueAlerts.repository.ThresholdMasterRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SmsaThresholdMasterService {
    
    private static final Logger logger = LogManager.getLogger(SmsaThresholdMasterService.class);
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private ThresholdMasterRepo thresholdMasterRepo;
    
    public List<ThresholdDTO> getFilteredMessages(ThresholdFilterPojo filters) {
        logger.info("Filtering Threshold data with filters: {}", filters);
        List<ThresholdDTO> pojoList = new ArrayList<>();
        
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SmsaThresholdMaster> query = cb.createQuery(SmsaThresholdMaster.class);
            Root<SmsaThresholdMaster> root = query.from(SmsaThresholdMaster.class);
            
            List<Predicate> predicates = buildDynamicPredicates(filters, cb, root);
            query.select(root).distinct(true);
            
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            
            TypedQuery<SmsaThresholdMaster> typedQuery = entityManager.createQuery(query);
            typedQuery.setHint("org.hibernate.fetchSize", 1000);
            
            try (Stream<SmsaThresholdMaster> stream = typedQuery.getResultList().stream()) {
                pojoList = stream.map(this::mapToPojo).collect(Collectors.toList());
            }
            
        } catch (Exception e) {
            logger.error("Error occurred while filtering Threshold data: {}", e.getMessage(), e);
        }
        
        return pojoList;
    }
    
    public Page<ThresholdDTO> getFilteredMessages(ThresholdFilterPojo filter, Pageable pageable) {
        logger.info("Executing paginated filter for Threshold data with filter: {}", filter);
        
        List<SmsaThresholdMaster> resultList = new ArrayList<>();
        long totalCount = 0;
        
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SmsaThresholdMaster> query = cb.createQuery(SmsaThresholdMaster.class);
            Root<SmsaThresholdMaster> root = query.from(SmsaThresholdMaster.class);
            
            List<Predicate> predicates = buildDynamicPredicates(filter, cb, root);
            query.select(root).distinct(true);
            
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            
            List<Order> orderOfSorting = new ArrayList<>();
            query.orderBy(orderOfSorting); // Optional: add sorting logic if required

            TypedQuery<SmsaThresholdMaster> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
            
            resultList = typedQuery.getResultList();
            
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SmsaThresholdMaster> countRoot = countQuery.from(SmsaThresholdMaster.class);
            List<Predicate> countPredicates = buildDynamicPredicates(filter, cb, countRoot);
            
            countQuery.select(cb.countDistinct(countRoot));
            if (!countPredicates.isEmpty()) {
                countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
            }
            
            totalCount = entityManager.createQuery(countQuery).getSingleResult();
            
        } catch (Exception e) {
            logger.error("Error during paginated threshold filtering: {}", e.getMessage(), e);
        }
        
        List<ThresholdDTO> pojoList = resultList.stream().map(this::mapToPojo).collect(Collectors.toList());
        return new PageImpl<>(pojoList, pageable, totalCount);
    }
    
    private List<Predicate> buildDynamicPredicates(ThresholdFilterPojo filter, CriteriaBuilder cb, Root<SmsaThresholdMaster> root) {
        List<Predicate> predicates = new ArrayList<>();
        
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(ThresholdFilterPojo.class).getPropertyDescriptors()) {
                String fieldName = pd.getName();
                if (Arrays.asList("class", "sortType", "columnSort", "generalSearch").contains(fieldName)) {
                    continue;
                }
                
                Object value = pd.getReadMethod().invoke(filter);
                if (value != null) {
                    logger.debug("Building predicate for field: {}, value: {}", fieldName, value);
                    
                    if (value instanceof List) {
                        List<?> rawList = ((List<?>) value).stream()
                                .filter(Objects::nonNull)
                                .filter(item -> !(item instanceof String) || !((String) item).trim().isEmpty())
                                .collect(Collectors.toList());
                        
                        if (!rawList.isEmpty()) {
                            Predicate predicate = buildPredicateForField(fieldName, rawList, cb, root);
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
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error building dynamic predicates: {}", e.getMessage(), e);
        }
        
        return predicates;
    }
    
    private Predicate buildPredicateForField(String fieldName, Object value, CriteriaBuilder cb, Root<SmsaThresholdMaster> root) {
        try {
            if (fieldName.endsWith("From") && value instanceof Comparable) {
                return cb.greaterThanOrEqualTo(root.get("fileDate"), (Comparable) value);
            }
            if (fieldName.endsWith("To") && value instanceof Comparable) {
                return cb.lessThanOrEqualTo(root.get("fileDate"), (Comparable) value);
            }
            if (value instanceof List<?>) {
                return handleListPredicate(fieldName, (List<?>) value, cb, root);
            }
            
            if (value instanceof String) {
                String str = ((String) value).trim();
                if (!str.isEmpty()) {
                    return cb.like(cb.lower(root.get(fieldName)), "%" + escapeLike(str.toLowerCase()) + "%");
                }
            }
            return cb.equal(root.get(fieldName), value);
        } catch (Exception e) {
            logger.warn("Failed to build predicate for field: {} with value: {}. Error: {}", fieldName, value, e.getMessage());
            return null;
        }
    }
    
    private Predicate handleListPredicate(String fieldName, List<?> list, CriteriaBuilder cb, Root<SmsaThresholdMaster> root) {
        if (list.isEmpty()) {
            return null;
        }
        
        if (list.get(0) instanceof String) {
            List<Predicate> likePredicates = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    Expression<String> fieldAsString = cb.function("TO_CHAR", String.class, root.get(fieldName));
                    likePredicates.add(cb.like(cb.lower(fieldAsString), "%" + escapeLike(item.toString().toLowerCase()) + "%"));
                }
            }
            return cb.or(likePredicates.toArray(new Predicate[0]));
        }
        
        return root.get(fieldName).in(list);
    }
    
    public List<ThresholdDTO> getThresholdMasterData() {
        logger.info("Fetching all Threshold Master Data");
        List<ThresholdDTO> pojoList = new ArrayList<>();
        
        try {
            List<SmsaThresholdMaster> data = thresholdMasterRepo.findByStatus("Active");
            pojoList = data.stream().map(this::mapToPojo).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving threshold master data: {}", e.getMessage(), e);
        }
        
        return pojoList;
    }
    
    private ThresholdDTO mapToPojo(SmsaThresholdMaster entity) {
        ThresholdDTO pojo = new ThresholdDTO();
        try {
            pojo.setThresholdId(entity.getThresholdId());
            pojo.setMsgCurrency(entity.getMsgCurrency());
            pojo.setSenderBic(entity.getSenderBic());
            pojo.setMsgType(entity.getMsgType());
            pojo.setCategoryAToAmount(entity.getCategoryAToAmount());
            pojo.setCategoryAFromAmount(entity.getCategoryAFromAmount());
            pojo.setCategoryBFromAmount(entity.getCategoryAFromAmount()); // Double-check if this is intentional
            pojo.setCategoryBToAmount(entity.getCategoryBToAmount());
            pojo.setCreatedBy(entity.getCreatedBy());
            pojo.setCreatedDate(entity.getCreatedDate());
            pojo.setModifiedBy(entity.getModifiedBy());
            pojo.setModifiedDate(entity.getModifiedDate());
            pojo.setVerifiedBy(entity.getVerifiedBy());
            pojo.setVerifiedDate(entity.getVerifiedDate());
        } catch (Exception e) {
            logger.error("Error mapping entity to DTO: {}", e.getMessage(), e);
        }
        return pojo;
    }
    
    private String escapeLike(String param) {
        return param.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("%", "\\%");
    }
}
