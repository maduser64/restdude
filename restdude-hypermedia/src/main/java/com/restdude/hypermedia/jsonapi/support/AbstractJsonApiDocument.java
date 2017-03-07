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
package com.restdude.hypermedia.jsonapi.support;

import com.restdude.hypermedia.jsonapi.JsonApiDocument;
import com.restdude.hypermedia.jsonapi.JsonApiLink;
import com.restdude.hypermedia.jsonapi.JsonApiResource;
import com.restdude.hypermedia.util.JsonApiModelBasedDocumentBuilder;
import com.restdude.mdd.model.ErrorModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 *
 * Abstract base class implementation of {@JsonApiDocument}
 *
 * @see JsonApiModelBasedDocumentBuilder
 * @see <a href="http://jsonapi.org/format/upcoming/#document-structure">JSON API Resources</a>
 *
 * @param <D> the JSON API Document data type
 */
public abstract class AbstractJsonApiDocument<D extends Object> implements JsonApiDocument<D> {

    @Getter @Setter private Map<String, JsonApiLink> links;
    private D data;
    private Collection<ErrorModel> errors;
    private Map<String, Serializable> meta;
    private Collection<JsonApiResource> included;


    public AbstractJsonApiDocument(D data){
        this.data = data;
    }

    protected AbstractJsonApiDocument() {

    }

    /**
     * @see JsonApiDocument#setData(java.lang.Object)
     */
    public D getData() {
        return data;
    }

    /**
     * @see JsonApiDocument#getData()
     */
    public void setData(D data) {
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<JsonApiResource> getIncluded() {
        return included;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIncluded(Collection<JsonApiResource> included) {
        this.included = included;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ErrorModel> getErrors() {
        return errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setErrors(Collection<ErrorModel> errors) {
        this.errors = errors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Serializable> getMeta() {
        return meta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMeta(Map<String, Serializable> meta) {
        this.meta = meta;
    }


}
