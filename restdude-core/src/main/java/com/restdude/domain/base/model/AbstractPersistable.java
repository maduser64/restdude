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
package com.restdude.domain.base.model;

import com.restdude.auth.spel.annotations.*;
import com.restdude.domain.base.validation.Unique;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Abstract entity class with basic auditing.
 * @param <ID> The id Serializable
 */
@XmlRootElement
@MappedSuperclass
@Unique
@EntityListeners(AuditingEntityListener.class)

@PreAuthorizeCount
@PreAuthorizeCreate
@PreAuthorizeDelete
@PreAuthorizeDeleteAll
@PreAuthorizeDeleteById
@PreAuthorizeDeleteWithCascade
@PreAuthorizeFindAll
@PreAuthorizeFindById
@PreAuthorizeFindByIds
@PreAuthorizeFindOrCreate
@PreAuthorizeFindPaginated
@PreAuthorizePatch
@PreAuthorizeUpdate
public abstract class AbstractPersistable<ID extends Serializable> implements CalipsoPersistable<ID> {

	private static final long serialVersionUID = -6009587976502456848L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPersistable.class);

    public static interface ItemView {}
    public static interface CollectionView {}




	public AbstractPersistable() {
		super();
	}
	
	public AbstractPersistable(ID id) {
		this.setId(id);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", this.getId()).toString();
	}

	/**
	 * Get the entity's primary key 
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	@Override
	public abstract ID getId();

	/**
	 * Set the entity's primary key
	 * @param id the id to set
	 */
	public abstract void setId(ID id);

	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractPersistable)) {
			return false;
		}
		AbstractPersistable other = (AbstractPersistable) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.getId(), other.getId());
		return builder.isEquals();
	}

	/**
	 *  @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getId() ? 0 : getId().hashCode() * 31;
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 */
	public void preSave() {

	}
}