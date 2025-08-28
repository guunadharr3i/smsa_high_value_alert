package com.smsa.highValueAlerts.service;

import com.smsa.highValueAlerts.DTO.SmsaAccountPojo;
import com.smsa.highValueAlerts.DTO.SmsaAccountReq;
import com.smsa.highValueAlerts.DTO.SwiftAccountSearchRequestPojo;
import com.smsa.highValueAlerts.entity.SmsaAccountMaster;
import com.smsa.highValueAlerts.entity.SmsaAccountTemp;
import com.smsa.highValueAlerts.repository.SmsaAccountRepository;
import com.smsa.highValueAlerts.repository.SmsaAccountTempRepo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class SmsaAccountService {

    @Autowired
    private SmsaAccountRepository masterRepository;

    @Autowired
    private SmsaAccountTempRepo smsaAccountTempRepo;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LogManager.getLogger(SmsaAccountService.class);

    public String processAccountOperations(SmsaAccountReq request) {
        String action = request.getActionType().toUpperCase();
        String accNum = request.getSmsaAccount().getAccountNO();

        switch (action) {
            case "ADD":
                if (masterRepository.findByAccountNO(accNum).isPresent()) {
                    throw new IllegalArgumentException("Account number '" + accNum + "' already exists in master. Cannot ADD.");
                }
                if (smsaAccountTempRepo.findByAccountNO(accNum).isPresent()) {
                    throw new IllegalArgumentException("Account number '" + accNum + "' already exists in pending request.");
                }
                SmsaAccountTemp addTemp = mapToTempEntity(request);
                smsaAccountTempRepo.save(addTemp);
                return "Account add request submitted.";

            case "EDIT":
                if (!masterRepository.findByAccountNO(accNum).isPresent()) {
                    throw new IllegalArgumentException("Account number '" + accNum + "' not found in master. Cannot EDIT.");
                }
                SmsaAccountTemp editTemp = mapToTempEntity(request);
                smsaAccountTempRepo.save(editTemp);
                return "Account edit request submitted.";

            case "DELETE":
                if (!masterRepository.findByAccountNO(accNum).isPresent()) {
                    throw new IllegalArgumentException("Account number '" + accNum + "' not found in master. Cannot DELETE.");
                }
                SmsaAccountTemp deleteTemp = mapToTempEntity(request);
                smsaAccountTempRepo.save(deleteTemp);
                return "Account delete request submitted.";

            case "APPROVE":
                Optional<SmsaAccountTemp> pendingOpt = smsaAccountTempRepo.findByAccountNO(accNum);
                if (!pendingOpt.isPresent()) {
                    throw new IllegalArgumentException("No pending request found to approve for account: " + accNum);
                }

                SmsaAccountTemp tempData = pendingOpt.get();
                String pendingAction = tempData.getActionType();

                if ("ADD".equalsIgnoreCase(pendingAction)) {
                    SmsaAccountMaster newAccount = mapToMasterEntity(tempData);
                    masterRepository.save(newAccount);
                } else if ("EDIT".equalsIgnoreCase(pendingAction)) {
                    SmsaAccountMaster existing = masterRepository.findByAccountNO(accNum).orElseThrow(()
                            -> new IllegalArgumentException("No master record found to edit."));
                    updateMasterFromTemp(existing, tempData);
                    masterRepository.save(existing);
                } else if ("DELETE".equalsIgnoreCase(pendingAction)) {
                    masterRepository.deleteByAccountNO(accNum);
                }

                smsaAccountTempRepo.delete(tempData);
                return "Request approved successfully.";

            case "REJECT":
                Optional<SmsaAccountTemp> rejectOpt = smsaAccountTempRepo.findByAccountNO(accNum);
                if (rejectOpt.isPresent()) {
                    smsaAccountTempRepo.delete(rejectOpt.get());
                    return "Request rejected and removed.";
                } else {
                    throw new IllegalArgumentException("No pending request found to reject for account: " + accNum);
                }

            default:
                throw new IllegalArgumentException("Invalid actionType: " + action);
        }
    }

    private SmsaAccountTemp mapToTempEntity(SmsaAccountReq req) {
        SmsaAccountTemp entity = new SmsaAccountTemp();
        entity.setActionType(req.getActionType());
        entity.setId(req.getSmsaAccount().getId());
        entity.setIo(req.getSmsaAccount().getIo());
        entity.setSenderBIC(req.getSmsaAccount().getSenderBIC());
        entity.setLocation(req.getSmsaAccount().getLocation());
        entity.setMessageTyp(req.getSmsaAccount().getMessageTyp());
        entity.setReceivedBIC(req.getSmsaAccount().getReceivedBIC());
        entity.setCurrency(req.getSmsaAccount().getCurrency());
        entity.setTeam(req.getSmsaAccount().getTeam());
        entity.setRemark(req.getSmsaAccount().getRemark());
        entity.setAccountNO(req.getSmsaAccount().getAccountNO());
        entity.setBankName(req.getSmsaAccount().getBankName());
        entity.setIsConfirm(req.getSmsaAccount().getIsConfirm());
        return entity;
    }

    private SmsaAccountMaster mapToMasterEntity(SmsaAccountTemp temp) {
        SmsaAccountMaster entity = new SmsaAccountMaster();
        entity.setId(temp.getId());
        entity.setIo(temp.getIo());
        entity.setSenderBIC(temp.getSenderBIC());
        entity.setLocation(temp.getLocation());
        entity.setMessageTyp(temp.getMessageTyp());
        entity.setReceivedBIC(temp.getReceivedBIC());
        entity.setCurrency(temp.getCurrency());
        entity.setTeam(temp.getTeam());
        entity.setRemark(temp.getRemark());
        entity.setAccountNO(temp.getAccountNO());
        entity.setBankName(temp.getBankName());
        entity.setIsConfirm(temp.getIsConfirm());
        return entity;
    }

    private void updateMasterFromTemp(SmsaAccountMaster master, SmsaAccountTemp temp) {
        master.setIo(temp.getIo());
        master.setSenderBIC(temp.getSenderBIC());
        master.setLocation(temp.getLocation());
        master.setMessageTyp(temp.getMessageTyp());
        master.setReceivedBIC(temp.getReceivedBIC());
        master.setCurrency(temp.getCurrency());
        master.setTeam(temp.getTeam());
        master.setRemark(temp.getRemark());
        master.setBankName(temp.getBankName());
        master.setIsConfirm(temp.getIsConfirm());
    }

    private SmsaAccountPojo updtePojoFromMaster(SmsaAccountMaster temp) {
        SmsaAccountPojo master = new SmsaAccountPojo();
        master.setIo(temp.getIo());
        master.setSenderBIC(temp.getSenderBIC());
        master.setLocation(temp.getLocation());
        master.setMessageTyp(temp.getMessageTyp());
        master.setReceivedBIC(temp.getReceivedBIC());
        master.setCurrency(temp.getCurrency());
        master.setTeam(temp.getTeam());
        master.setRemark(temp.getRemark());
        master.setBankName(temp.getBankName());
        return master;
    }

    public Page<SmsaAccountPojo> searchMasterAccountData(SwiftAccountSearchRequestPojo acc, Pageable pageable) {
        List<SmsaAccountMaster> resultList = new ArrayList<>();
        long totalCount = 0;
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SmsaAccountMaster> query = cb.createQuery(SmsaAccountMaster.class);
            Root<SmsaAccountMaster> root = query.from(SmsaAccountMaster.class);
            List<Predicate> predicates = buildDynamicPredicates(acc, cb, root);
            query.select(root).distinct(true);
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            List<Order> orderOfSorting = new ArrayList<>();

            orderOfSorting.add(cb.desc(root.get("accountNO")));

            query.orderBy(orderOfSorting);

            TypedQuery<SmsaAccountMaster> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
            resultList = typedQuery.getResultList();

            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SmsaAccountMaster> countRoot = countQuery.from(SmsaAccountMaster.class);
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

    private List<Predicate> buildDynamicPredicates(SwiftAccountSearchRequestPojo filter, CriteriaBuilder cb, Root<SmsaAccountMaster> root) {
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

    private Predicate buildPredicateForField(String fieldName, Object value, CriteriaBuilder cb, Root<SmsaAccountMaster> root) {
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

    private Predicate handleListPredicate(String fieldName, List<?> list, CriteriaBuilder cb, Root<SmsaAccountMaster> root) {
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
