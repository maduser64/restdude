/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.mdd.util;

import com.restdude.domain.base.validation.CaseSensitive;
import com.restdude.mdd.annotation.ModelRelatedResource;
import com.restdude.mdd.annotation.ModelResource;
import com.restdude.util.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EntityUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityUtil.class);

    private static ConcurrentHashMap<String, Boolean> fieldCaseSensitivity = new ConcurrentHashMap<String, Boolean>();

	@SuppressWarnings("unchecked")
	public static <T> T getParentEntity(Object child){
        ModelRelatedResource anr = child.getClass().getAnnotation(ModelRelatedResource.class);
        Assert.notNull( anr, "Given child object has no @RelatedEntity annotation");
        Field field = ReflectionUtils.findField(child.getClass(), anr.parentProperty());
        field.setAccessible(true);
        Object parent = ReflectionUtils.getField(field, child);
        return (T) parent;
    }

	public static Set<BeanDefinition> findModelResources(String scanPackage) {
		ClassPathScanningCandidateComponentProvider provider = createComponentScanner(ModelResource.class, ModelRelatedResource.class);
		return provider.findCandidateComponents(scanPackage);
	}

	private static Set<BeanDefinition> findEntities(String scanPackage) {
		ClassPathScanningCandidateComponentProvider provider = createComponentScanner(Entity.class);
		return provider.findCandidateComponents(scanPackage);
	}

	public static Set<String> findEntityPackageNames(String... basePackages) {
		Set<String> pkgs = new HashSet<String>();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> entityBeanDefs = EntityUtil.findEntities(basePackage);
			for (BeanDefinition beanDef : entityBeanDefs) {
				Class<?> entity = ClassUtils.getClass(beanDef.getBeanClassName());
				pkgs.add(entity.getPackage().getName());
			}
		}
		return pkgs;
	}

	public static ClassPathScanningCandidateComponentProvider createComponentScanner(Class... annotations) {
		// Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
		for (Class annotation : annotations) {
			provider.addIncludeFilter(new AnnotationTypeFilter(annotation));
		}
		return provider;
    }
    
    public static Class<?> getIdType(Class<?> modelType) {
    	Class<?> idType = null;
		Method testMethod = null;
        try {
			testMethod = modelType.getMethod("getId");
		} catch (Exception e) {
			LOGGER.error("Could not determine ID type", e);
		}
		if(testMethod != null){
			idType = testMethod.getReturnType();
		}
		return idType;
	}
    
	public static String[] getNullPropertyNames (Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for(java.beans.PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}

    public static boolean isCaseSensitive(Class domainClass, String propertyName) {
        boolean caseSensitive = false;
        String fieldKey = new StringBuffer(domainClass.getCanonicalName()).append('.').append(propertyName).toString();


        if (!fieldCaseSensitivity.containsKey(fieldKey)) {
            Field field = FieldUtils.getField(domainClass, propertyName, true);

            if (field != null && String.class.isAssignableFrom(field.getType())) {

                CaseSensitive annotation = field.getAnnotation(CaseSensitive.class);
                if (annotation != null) {
                    caseSensitive = annotation.value();
                }
                fieldCaseSensitivity.put(fieldKey, caseSensitive);
            }
        } else {
            caseSensitive = fieldCaseSensitivity.get(fieldKey);
        }

        return caseSensitive;
    }
}
