/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.specification;

import com.restdude.mdd.model.Model;
import com.restdude.mdd.registry.ModelInfo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * A {@link Specification} implementation that will dynamically resolve and use an appropriate {@link IPredicateFactory} to delegate the creation of a predicate
 * @param <T> the {@link Root} entity model type
 */
@Slf4j
public class PredicateFactorySpecification<T extends Model> implements Specification<T> {

    private final ConversionService conversionService;
    private final ModelInfo modelInfo;
    private final String propertyPath;
    private final PredicateOperator operator;
    private final List<String> propertyValues;

    public PredicateFactorySpecification(
            @NonNull ConversionService conversionService, @NonNull ModelInfo modelInfo, @NonNull String propertyPath, @NonNull PredicateOperator operator, @NonNull List<String> propertyValues) {
        super();
        this.conversionService = conversionService;
        this.modelInfo = modelInfo;
        // remove unnecessary identifier suffix if any
        if (propertyPath.endsWith(".pk")) {
            propertyPath = propertyPath.substring(0, propertyPath.length() - 3);
        }
        this.propertyPath = propertyPath;
        this.operator = operator;
        this.propertyValues = propertyValues;
    }

    @Override
    public Predicate toPredicate(
            Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = null;
        IPredicateFactory predicateFactory = null;
        Class fieldType = SpecificationUtils.getMemberType(this.modelInfo.getModelType(), this.propertyPath);
        if (fieldType != null) {
             predicateFactory = SpecificationUtils.getPredicateFactoryForClass(fieldType);
            if (predicateFactory != null) {
                // TODO: add operator support to factories
                predicate = predicateFactory.buildPredicate(root, builder, this.propertyPath, fieldType, conversionService, this.propertyValues.toArray(new String[this.propertyValues.size()]));
            }
        }
/*
        List<Object> args = castArguments(root);
        Object argument = args.get(0);
        switch (RsqlSpecOperation.getSimpleOperator(operator)) {

            case EQUAL: {
                if (argument instanceof String) {
                    return builder.like(
                            root.<String> get(property), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNull(root.get(property));
                } else {
                    return builder.equal(root.get(property), argument);
                }
            }
            case NOT_EQUAL: {
                if (argument instanceof String) {
                    return builder.notLike(
                            root.<String> get(property), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNotNull(root.get(property));
                } else {
                    return builder.notEqual(root.get(property), argument);
                }
            }
            case GREATER_THAN: {
                return builder.greaterThan(root.<String> get(property), argument.toString());
            }
            case GREATER_THAN_OR_EQUAL: {
                return builder.greaterThanOrEqualTo(
                        root.<String> get(property), argument.toString());
            }
            case LESS_THAN: {
                return builder.lessThan(root.<String> get(property), argument.toString());
            }
            case LESS_THAN_OR_EQUAL: {
                return builder.lessThanOrEqualTo(
                        root.<String> get(property), argument.toString());
            }
            case IN:
                return root.get(property).in(args);
            case NOT_IN:
                return builder.not(root.get(property).in(args));
        }
*/
        return predicate;
    }

}