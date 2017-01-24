/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
package com.restdude.domain.geography.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restdude.domain.base.model.AbstractPersistable;
import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.mdd.annotation.ModelResource;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Abstract base class for formal (usually political) geographical regions
 */
@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@ModelResource(path = "countries")
@ApiModel(value = "Region", description = "A model representing a geographcal region.")
public abstract class AbstractFormalRegion<P extends AbstractFormalRegion> extends AbstractPersistable<String> implements CalipsoPersistable<String> {

	private static final String PATH_SEPARATOR = ": ";

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;
	@NotNull
	@Column(name = "path", nullable = false)
	private String path;
	@NotNull
	@Column(name = "path_level", nullable = false)
	private Short pathLevel;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="parent")
	private P parent;

	public AbstractFormalRegion() {
		super();
	}


	public AbstractFormalRegion(String pk, String name, P parent) {
		this.setPk(pk);
		this.name = name;
		this.parent = parent;
	}

	@JsonIgnore
	@Transient
	public String getPathSeparator(){
		return PATH_SEPARATOR;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void preSave() {
        // set path
		if(this.getPath() == null){
			StringBuffer path = new StringBuffer();
			if(this.getParent() != null){
				path.append(this.getParent().getPath());
			}
			path.append(getPathSeparator());
			path.append(this.getName());
			this.setPath(path.toString());
		}
		// set path level
		Integer pathLevel = StringUtils.countMatches(this.getPath(), getPathSeparator());
		this.setPathLevel(pathLevel.shortValue());
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public P getParent() {
		return parent;
	}
	public void setParent(P parent) {
		this.parent = parent;
	}
	public Short getPathLevel() {
		return pathLevel;
	}
	public void setPathLevel(Short pathLevel) {
		this.pathLevel = pathLevel;
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(AbstractFormalRegion.class)
				.append(this.getName())
				.append(this.getPath())
				.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof AbstractFormalRegion) {
			final AbstractFormalRegion other = (AbstractFormalRegion) obj;
			return new EqualsBuilder()
					.append(this.getName(), other.getName())
					.append(this.getPath(), other.getPath())
					.isEquals();
		} else {
			return false;
		}
	}
}
