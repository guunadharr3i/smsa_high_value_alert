/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.RecepientDTO;
import com.smsa.highValueAlerts.DTO.RecepientFilterPojo;
import com.smsa.highValueAlerts.entity.SmsaRecepientMaster;
import com.smsa.highValueAlerts.repository.RecepientMasterRepo;

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

/**
 *
 * @author abcom
 */
public class SmsaRecepientMasterService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RecepientMasterRepo recepientMasterRepo;

    private static final Logger logger = LogManager.getLogger(SmsaRecepientMasterService.class);

    public Page<RecepientDTO> getFilteredMessages(RecepientFilterPojo filter, Pageable pageable) {
        logger.info("Executing getFilteredMessages with filter: {}", filter);

        List<SmsaRecepientMaster> resultList = new ArrayList<>();
        long totalCount = 0;

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            CriteriaQuery<SmsaRecepientMaster> query = cb.createQuery(SmsaRecepientMaster.class);
            Root<SmsaRecepientMaster> root = query.from(SmsaRecepientMaster.class);
            List<Predicate> predicates = buildDynamicPredicates(filter, cb, root);

            query.select(root).distinct(true);
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            List<Order> orderOfSorting = new ArrayList<>();

            // if (!filter.getColumnSort().contains("fileDate")) {
            // filter.getColumnSort().add("fileDate");
            // }
            //
            // if (filter.getColumnSort() != null && !filter.getColumnSort().isEmpty()) {
            // logger.info("Sortimg by columns: " + "fileDate");
            // for (String column : filter.getColumnSort()) {
            // logger.info("," + column);
            // if (filter.getSortType().equals("DESC")) {
            // orderOfSorting.add(cb.desc(root.get(column)));
            // } else {
            // orderOfSorting.add(cb.asc(root.get(column)));
            // }
            // }
            // }
            query.orderBy(orderOfSorting);

            TypedQuery<SmsaRecepientMaster> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
            resultList = typedQuery.getResultList();

            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SmsaRecepientMaster> countRoot = countQuery.from(SmsaRecepientMaster.class);
            List<Predicate> countPredicates = buildDynamicPredicates(filter, cb, countRoot);

            countQuery.select(cb.countDistinct(countRoot));
            if (!countPredicates.isEmpty()) {
                countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
            }

            totalCount = entityManager.createQuery(countQuery).getSingleResult();

        } catch (Exception e) {
            logger.error("Exception occurred while filtering Swift messages: {}", e.getMessage(), e);
        }

        List<RecepientDTO> pojoList = resultList.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());

        return new PageImpl<>(pojoList, pageable, totalCount);
    }

    private List<Predicate> buildDynamicPredicates(RecepientFilterPojo filter, CriteriaBuilder cb,
            Root<SmsaRecepientMaster> root) {
        List<Predicate> predicates = new ArrayList<>();

        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(RecepientDTO.class).getPropertyDescriptors()) {
                String fieldName = pd.getName();
                Object value;

                if (!"class".equals(fieldName) && !"sortType".equals(fieldName) && !"columnSort".equals(fieldName)
                        && !"generalSearch".equals(fieldName)) {
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
                            Predicate predicate = buildPredicateForField(fieldName, value, cb, root);
                            if (predicate != null) {
                                predicates.add(predicate);
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

    private Predicate buildPredicateForField(String fieldName, Object value, CriteriaBuilder cb,
            Root<SmsaRecepientMaster> root) {
        if (fieldName.endsWith("From") && value instanceof Comparable) {
            return cb.greaterThanOrEqualTo(root.get("fileDate"), (Comparable) value);
        }

        if (fieldName.endsWith("To") && value instanceof Comparable) {
            return cb.lessThanOrEqualTo(root.get("fileDate"), (Comparable) value);
        }

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

    private Predicate handleListPredicate(String fieldName, List<?> list, CriteriaBuilder cb,
            Root<SmsaRecepientMaster> root) {
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
                            cb.like(cb.lower(fieldAsString), "%" + escapeLike(item.toString().toLowerCase()) + "%"));
                }
            }
            return cb.or(likePredicates.toArray(new Predicate[0]));
        }

        return root.get(fieldName).in(list);
    }

    public List<RecepientDTO> getRecepientMasterData() {
        List<SmsaRecepientMaster> data = recepientMasterRepo.findAll();
        List<RecepientDTO> pojoList = data.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());
        return pojoList;
    }

    private RecepientDTO mapToPojo(SmsaRecepientMaster entity) {
        RecepientDTO pojo = new RecepientDTO();
        pojo.setSmsaRamId(entity.getSmsaRamId());
        pojo.setSmsaEmpId(entity.getSmsaEmpId());
        pojo.setSmsaGeoName(entity.getSmsaGeoName());
        pojo.setSmsaSenderBic(entity.getSmsaSenderBic());
        pojo.setSmsaMsgType(entity.getSmsaMsgType());
        pojo.setSmsaEmpName(entity.getSmsaEmpName());
        pojo.setSmsaGrade(entity.getSmsaGrade());
        pojo.setSmsaCreatedBy(entity.getSmsaCreatedBy());
        pojo.setSmsaModifiedBy(entity.getSmsaModifiedBy());
        pojo.setSmsaModifiedDate(entity.getSmsaModifiedDate());
        pojo.setSmsaVerifiedBy(entity.getSmsaVerifiedBy());
        pojo.setSmsaVerifiedDate(entity.getSmsaVerifiedDate());

        return pojo;
    }

    private String escapeLike(String param) {
        return param.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("%", "\\%");
    }

}
