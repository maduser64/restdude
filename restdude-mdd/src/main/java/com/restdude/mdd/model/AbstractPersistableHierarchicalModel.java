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
package com.restdude.mdd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * A base class for pathFragment-like resource entities: files, folders, categories etc.
 */
@MappedSuperclass
public abstract class AbstractPersistableHierarchicalModel<T extends AbstractPersistableHierarchicalModel<T>> extends AbstractPersistableNamedModel {

	private static final long serialVersionUID = 1L;

	private static final String PATH_SEPARATOR = "/";

	/**
	 * The HTTP URL of the resource, excluding the protocol, domain and port. Starts with a slash. 
	 */
    @NotNull
    @Column(name = "resource_path", length = 1500, nullable = false)
	private String path;
	
	/**
	 * The number of URL segments in the resource path
	 */
    @NotNull
    @Column(name = "path_level", nullable = false)
	private Short pathLevel;

	@DiffIgnore
	@JsonIgnore
	@ManyToOne(/* cascade=CascadeType.ALL, */fetch = FetchType.EAGER)
    @JoinColumn(name = "same_as", referencedColumnName = "pk", nullable = true)
    private T sameAs;

	@DiffIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent", referencedColumnName = "pk", nullable = true)
    private T parent;

	@DiffIgnore
	@JsonIgnore
	@OneToMany(mappedBy = "parent", /* cascade=CascadeType.ALL, */ fetch=FetchType.LAZY)
	private List<T> children = new ArrayList<T>(0);

	public AbstractPersistableHierarchicalModel() {
		super();
	}
	public AbstractPersistableHierarchicalModel(String name) {
		this.setName(name);
	}
	public AbstractPersistableHierarchicalModel(String name, T parent) {
		this(name);
		this.setParent(parent);
	}
	
	@JsonIgnore
	@Transient
	protected String getPathSeparator(){
		return PATH_SEPARATOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preSave() {
		// set pathFragment
		if(this.getPath() == null){
			StringBuffer path = new StringBuffer();
			if(this.getParent() != null){
				path.append(this.getParent().getPath());
			}
			path.append(getPathSeparator());
			path.append(this.getName());
			this.setPath(path.toString());
		}
		// set pathFragment level
		Integer pathLevel = StringUtils.countMatches(this.getPath(), getPathSeparator());
		this.setPathLevel(pathLevel.shortValue());;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!this.getClass().isAssignableFrom(obj.getClass())) {
			return false;
		}
		
		AbstractPersistableHierarchicalModel other = (AbstractPersistableHierarchicalModel) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.appendSuper(super.equals(obj));
		builder.append(this.getPath(), other.getPath());
		return builder.isEquals();
	}
	
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Short getPathLevel() {
		return pathLevel;
	}
	public void setPathLevel(Short pathLevel) {
		this.pathLevel = pathLevel;
	}
	public T getSameAs() {
		return sameAs;
	}
	public void setSameAs(T sameAs) {
		this.sameAs = sameAs;
	}
	public T getParent() {
		return parent;
	}
	public void setParent(T parent) {
		this.parent = parent;
	}
	public List<T> getChildren() {
		return children;
	}
	public void setChildren(List<T> children) {
		this.children = children;
	}

}