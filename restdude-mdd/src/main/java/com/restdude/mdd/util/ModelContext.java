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
package com.restdude.mdd.util;

import com.restdude.mdd.controller.AbstractModelController;
import com.restdude.domain.base.annotation.model.ModelRelatedResource;
import com.restdude.domain.base.annotation.model.ModelResource;
import com.restdude.mdd.registry.ModelInfo;
import com.restdude.mdd.specifications.IPredicateFactory;
import com.restdude.util.ClassUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.ManyToAny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Adapter-ish context class for classes with {@link ModelResource}
 * and {@link ModelRelatedResource}
 * annotations.
 */
public final class ModelContext {
	
	private static final String AUDITABLE2 = "auditable";

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelContext.class);
    private ModelInfo modelInfo;


    private Class<?> parentClass;
    private String parentProperty;

    @Getter
    private String generatedClassNamePrefix;

    @Getter
    private Class<?> repositoryType, serviceInterfaceType, serviceImplType;

    @Getter @Setter
    private BeanDefinition repositoryDefinition, serviceDefinition;
    @Getter
    private BeanDefinition controllerDefinition;

	private Map<String, Object> apiAnnotationMembers;

	private ModelResource modelResource;

	@Getter
	private boolean auditable;


	private ModelContext() {

	}

    public ModelContext(@NonNull ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
        Class<?> modelClass = modelInfo.getModelType();

        this.generatedClassNamePrefix = modelClass.getSimpleName().replace("Model", "").replace("Entity", "");
        this.modelResource = modelClass.getAnnotation(ModelResource.class);
		this.generatedClassNamePrefix = modelClass.getSimpleName().replace("Model", "").replace("Entity", "");
        if(this.modelResource != null){
            this.parentClass = null;
            this.parentProperty = null;
        }
    }

	public List<Class<?>> getGenericTypes() {
		List<Class<?>> genericTypes = new LinkedList<Class<?>>();
		genericTypes.add(this.getModelType());
		if (this.getModelIdType() != null) {
			genericTypes.add(this.getModelIdType());
		}
		return genericTypes;
	}


	public Class getControllerSuperClass(){
		Class sClass = this.modelResource.controllerSuperClass();
		if(sClass == null || Object.class.equals(sClass)){
			sClass = AbstractModelController.class;
		}
		return  sClass;
	}

	public void setServiceInterfaceType(Class<?> serviceInterfaceType) {
		this.serviceInterfaceType = serviceInterfaceType;
	}

	public void setServiceImplType(Class<?> serviceImplType) {
		this.serviceImplType = serviceImplType;
	}

	public void setRepositoryType(Class<?> repositoryType) {
		this.repositoryType = repositoryType;
	}

    public boolean isNested(){
        return parentClass != null;
    }

	public boolean isNestedCollection(){
        if( !isNested() ){
            return false;
        }
        Class<?> modelType = this.modelInfo.getModelType();
        ModelRelatedResource anr = modelType.getAnnotation(ModelRelatedResource.class);
        Assert.notNull(anr, "Not a nested resource");

        String parentProperty = anr.parentProperty();
        Field field = ReflectionUtils.findField(modelType, parentProperty);
        if( hasAnnotation(field, OneToOne.class, org.hibernate.mapping.OneToOne.class) ){
            return false;
        }else if( hasAnnotation(field, ManyToOne.class, org.hibernate.mapping.ManyToOne.class,
                ManyToMany.class, ManyToAny.class) ){ // TODO handle more mappings here?
            return true;
        }

        throw new IllegalStateException("No known mapping found");

    }

    private boolean hasAnnotation( Field field, Class<?>... annotations){

        for( Class<?> a : annotations ){
            if( field.isAnnotationPresent( (Class<Annotation>) a) ){
                return true;
            }
        }
        return false;
    }


    public Map<String, Object> getApiAnnotationMembers(){
    	// init if needed
    	if(this.apiAnnotationMembers == null){
			apiAnnotationMembers = new HashMap<>();

			Class<?> modelType = this.getModelType();
			ModelResource resource = modelType.getAnnotation(ModelResource.class);
			if( resource != null ){
				// auditable?
				apiAnnotationMembers.put(AUDITABLE2, resource.auditable());
				// get tags (grouping key, try API name)
				if(StringUtils.isNotBlank(resource.apiName())){
					String[] tags = {resource.apiName()};
					apiAnnotationMembers.put("tags", tags);
				}
				// or pathFragment
				else if(StringUtils.isNotBlank(resource.pathFragment())){

					String[] tags = {resource.pathFragment()};
					apiAnnotationMembers.put("tags", tags);
				}
				// or simple name
				else{
					String[] tags = {StringUtils.join(
							StringUtils.splitByCharacterTypeCamelCase(modelType.getSimpleName()),
							' '
					)};
					apiAnnotationMembers.put("tags", tags);
				}
				// add description
				if(StringUtils.isNotBlank(resource.apiDescription())){
					apiAnnotationMembers.put("description", resource.apiDescription());
				}
			}else{
				throw new IllegalStateException("Not an entity");
			}
		}

        return apiAnnotationMembers.size() > 0 ? apiAnnotationMembers : null;
    }


	public Class<?> getModelIdType() {
		return this.modelInfo.getIdentifierType();
	}

	public Class<?> getModelType() {
		return this.modelInfo.getModelType();
	}

	public String getName() {
		return this.modelInfo.getPackageName();
	}

	public String getBeansBasePackage() {
		return this.modelInfo.getBeansBasePackage();
	};

	public void setPredicateFactory(IPredicateFactory predicateFactory) {
		this.modelInfo.setPredicateFactory(predicateFactory);
	}

	public void setControllerDefinition(BeanDefinition controllerDefinition) {
		this.controllerDefinition = controllerDefinition;
		this.modelInfo.setModelControllerType(ClassUtils.getClass(controllerDefinition.getBeanClassName()));
	}

}
