/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.mdd.specifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class LocalDatePredicateFactory extends AbstractPredicateFactory<LocalDate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDatePredicateFactory.class);

    public LocalDatePredicateFactory() {
    }

    /**
     * @see IPredicateFactory#getPredicate(Root, CriteriaBuilder, String, Class, ConversionService, String[])
     */
    @Override
    public Predicate getPredicate(Root<?> root, CriteriaBuilder cb, String propertyName, Class<LocalDate> fieldType, ConversionService conversionService, String[] propertyValues) {
        Predicate predicate = null;

        try {
            LOGGER.debug("getPredicate, propertyName: {}, fieldType: {}, root: {}", propertyName, fieldType, root);

            Path path = this.<LocalDate>getPath(root, propertyName, fieldType);
            if (propertyValues.length == 0) {
                predicate = path.isNull();
            }
            if (propertyValues.length == 1) {
                LocalDate date = conversionService.convert(propertyValues[0], LocalDate.class);
                predicate = date != null ? cb.equal(path, date) : path.isNull();
            } else if (propertyValues.length == 2) {
                LocalDate from = conversionService.convert(propertyValues[0], LocalDate.class);
                LocalDate to = conversionService.convert(propertyValues[1], LocalDate.class);
                predicate = cb.between(path, from, to);
            } else {
                Set<LocalDate> values = new HashSet<>();
                for (int i = 0; i < propertyValues.length; i++) {
                    LocalDate d = conversionService.convert(propertyValues[i], LocalDate.class);
                    values.add(d);
                }
                predicate = path.in(values);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return predicate;
    }
}