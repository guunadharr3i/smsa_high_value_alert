package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.RecepientDTO;
import com.smsa.highValueAlerts.DTO.RecepientFilterPojo;
import com.smsa.highValueAlerts.entity.SmsaRecepientMaster;
import com.smsa.highValueAlerts.repository.RecepientMasterRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SmsaRecepientMasterService {

    private static final Logger logger = LogManager.getLogger(SmsaRecepientMasterService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RecepientMasterRepo recepientMasterRepo;

    public List<RecepientDTO> getFilteredMessages(RecepientFilterPojo filters) {
        logger.info("Started getFilteredMessages without pagination. Filter: {}", filters);
        List<RecepientDTO> pojoList = new ArrayList<>();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SmsaRecepientMaster> query = cb.createQuery(SmsaRecepientMaster.class);
            Root<SmsaRecepientMaster> root = query.from(SmsaRecepientMaster.class);

            List<Predicate> predicates = buildDynamicPredicates(filters, cb, root);
            query.select(root).distinct(true);

            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<SmsaRecepientMaster> typedQuery = entityManager.createQuery(query);
            typedQuery.setHint("org.hibernate.fetchSize", 1000);

            try (Stream<SmsaRecepientMaster> stream = typedQuery.getResultList().stream()) {
                pojoList = stream.map(this::mapToPojo).collect(Collectors.toList());
            }

        } catch (Exception e) {
            logger.error("Exception occurred while filtering recipient data: {}", e.getMessage(), e);
        }

        logger.info("Returning {} recipient records", pojoList.size());
        return pojoList;
    }

    public Page<RecepientDTO> getFilteredMessages(RecepientFilterPojo filter, Pageable pageable) {
        logger.info("Started getFilteredMessages with pagination. Filter: {}", filter);
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

            // Sorting (if required)
            List<Order> orderOfSorting = new ArrayList<>();
            query.orderBy(orderOfSorting); // Placeholder

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
            logger.error("Exception during paginated filter fetch: {}", e.getMessage(), e);
        }

        List<RecepientDTO> dtoList = resultList.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());

        logger.info("Returning {} records out of total {}", dtoList.size(), totalCount);
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private List<Predicate> buildDynamicPredicates(RecepientFilterPojo filter, CriteriaBuilder cb,
                                                   Root<SmsaRecepientMaster> root) {
        List<Predicate> predicates = new ArrayList<>();
        logger.debug("Building predicates for filter: {}", filter);

        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(RecepientFilterPojo.class).getPropertyDescriptors()) {
                String fieldName = pd.getName();
                Object value;

                if (!"class".equals(fieldName) && !"sortType".equals(fieldName)
                        && !"columnSort".equals(fieldName) && !"smsaAction".equals(fieldName)
                        && !"generalSearch".equals(fieldName)) {

                    value = pd.getReadMethod().invoke(filter);
                    if (value != null) {
                        logger.debug("Processing field: {}, value: {}", fieldName, value);

                        if (value instanceof List) {
                            List<?> rawList = (List<?>) value;
                            List<?> filteredList = rawList.stream()
                                    .filter(Objects::nonNull)
                                    .filter(item -> !(item instanceof String) || !((String) item).trim().isEmpty())
                                    .collect(Collectors.toList());

                            if (!filteredList.isEmpty()) {
                                Predicate p = buildPredicateForField(fieldName, filteredList, cb, root);
                                if (p != null) predicates.add(p);
                            }
                        } else {
                            Predicate p = buildPredicateForField(fieldName, value, cb, root);
                            if (p != null) predicates.add(p);
                        }
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error while building predicates: {}", e.getMessage(), e);
        }

        logger.debug("Total predicates built: {}", predicates.size());
        return predicates;
    }

    private Predicate buildPredicateForField(String fieldName, Object value, CriteriaBuilder cb,
                                             Root<SmsaRecepientMaster> root) {
        try {
            if (fieldName.endsWith("From") && value instanceof Comparable) {
                return cb.greaterThanOrEqualTo(root.get("fileDate"), (Comparable) value);
            }

            if (fieldName.endsWith("To") && value instanceof Comparable) {
                return cb.lessThanOrEqualTo(root.get("fileDate"), (Comparable) value);
            }

            if (value instanceof List && !((List<?>) value).isEmpty()) {
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
        } catch (Exception e) {
            logger.error("Error creating predicate for field {}: {}", fieldName, e.getMessage(), e);
            return null;
        }
    }

    private Predicate handleListPredicate(String fieldName, List<?> list, CriteriaBuilder cb,
                                          Root<SmsaRecepientMaster> root) {
        try {
            if (list.isEmpty()) return null;

            List<Predicate> likePredicates = new ArrayList<>();

            if (list.get(0) instanceof String) {
                for (Object item : list) {
                    if (item != null) {
                        Expression<String> fieldAsString = cb.function("TO_CHAR", String.class, root.get(fieldName));
                        likePredicates.add(cb.like(cb.lower(fieldAsString),
                                "%" + escapeLike(item.toString().toLowerCase()) + "%"));
                    }
                }
                return cb.or(likePredicates.toArray(new Predicate[0]));
            }

            return root.get(fieldName).in(list);

        } catch (Exception e) {
            logger.error("Error handling list predicate for {}: {}", fieldName, e.getMessage(), e);
            return null;
        }
    }

    public List<RecepientDTO> getRecepientMasterData() {
        logger.info("Fetching all Recepient Master Data");
        List<RecepientDTO> pojoList = new ArrayList<>();

        try {
            List<SmsaRecepientMaster> data = recepientMasterRepo.findAll();
            pojoList = data.stream()
                    .map(this::mapToPojo)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching Recepient Master data: {}", e.getMessage(), e);
        }

        logger.info("Fetched {} records from RecepientMasterRepo", pojoList.size());
        return pojoList;
    }

    private RecepientDTO mapToPojo(SmsaRecepientMaster entity) {
        RecepientDTO pojo = new RecepientDTO();
        try {
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
