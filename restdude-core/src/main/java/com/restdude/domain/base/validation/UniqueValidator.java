package com.restdude.domain.base.validation;

import com.restdude.auth.userdetails.controller.form.ValidatorUtil;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.mdd.util.EntityUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.proxy.HibernateProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, CalipsoPersistable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueValidator.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void initialize(Unique annotation) {
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(CalipsoPersistable value, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("isValid value: {}", value);
        boolean valid = true;
        // skip validation if null
        if (value != null) {

            // get the entity class being proxied by <code>value</code> if any, or the actual <code>value</code> class otherwise
            Class domainClass = HibernateProxyHelper.getClassWithoutInitializingProxy(value);

            try {
                // get unique field names
                List<String> uniqueFieldNames = ValidatorUtil.getUniqueFieldNames(domainClass);

                // get records matching the unique field values
                List<CalipsoPersistable> resultSet = getViolatingRecords(value, domainClass, uniqueFieldNames);

                LOGGER.debug("isValid, resultSet size: {}", resultSet.size());

                // process violating records
                if (!resultSet.isEmpty()) {

                    // disable default constraint validation construction
                    // as it will point to  the object instead of the property
                    constraintValidatorContext.disableDefaultConstraintViolation();

                    for (CalipsoPersistable match : resultSet) {
                        if (!match.getId().equals(value.getId())) {
                            for (String propertyName : uniqueFieldNames) {
                                Object newValue = PropertyUtils.getProperty(value, propertyName);
                                Object existingValue = PropertyUtils.getProperty(match, propertyName);
                                if (newValue != null) {
                                    // ignore case for strings?
                                    if (newValue instanceof String && !EntityUtil.isCaseSensitive(domainClass, propertyName)) {
                                        newValue = ((String) newValue).toLowerCase();
                                        existingValue = existingValue != null ? ((String) existingValue).toLowerCase() : null;
                                    }
                                    // match?
                                    if (newValue.equals(existingValue)) {
                                        LOGGER.debug("isValid, adding violation for property name: {}, value: {}", propertyName, newValue);
                                        valid = false;
                                        // report violation
                                        constraintValidatorContext
                                                .buildConstraintViolationWithTemplate("Unique value not available")
                                                .addPropertyNode(propertyName).addConstraintViolation();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error while validating constraints", e);
            }
        }

        LOGGER.debug("isValid returns: {}", valid);
        return valid;

    }

    private List<CalipsoPersistable> getViolatingRecords(CalipsoPersistable value, Class domainClass, List<String> uniqueFieldNames) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        LOGGER.debug("getViolatingRecords, for value: {}, uniqueFieldNames: {}", value, uniqueFieldNames);
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(domainClass);
        Root<? extends CalipsoPersistable<Serializable>> root = criteriaQuery.from(domainClass);
        List<Predicate> predicates = new ArrayList<Predicate>(uniqueFieldNames.size());
        for (String propertyName : uniqueFieldNames) {
            LOGGER.debug("getViolatingRecords, adding predicate for field: {}", propertyName);
            Object propertyValue = PropertyUtils.getProperty(value, propertyName);
            Predicate predicate = criteriaBuilder.equal(root.get(propertyName), propertyValue);
            predicates.add(predicate);
        }

        criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<CalipsoPersistable> typedQuery = this.entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

}