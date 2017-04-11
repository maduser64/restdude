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
package com.restdude.domain;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

/**
 * Base interface for model objects
 * @param <PK> The primary key type, if any
 */
public interface Model<PK extends Serializable> extends Serializable {

    /**
     * The primary key, field name.
     */
    String PK_FIELD_NAME = "pk";

    /**
     * Get the entity's primary key. Functionally equivalent to {@linke org.springframework.data.domain.Persistable#getPk()}
     * only without conflict with {@link ResourceSupport#getId()}
     */
    //@JsonGetter("id")
    PK getPk();

    /**
     * Set the entity's primary key. Functionally equivalent to {@linke org.springframework.data.domain.Persistable#setPk()}
     * only without conflict with the getter {@link ResourceSupport#getId()}
     *
     * @param pk the pk to set
     */
    //@JsonSetter("id")
    void setPk(PK pk);

    interface ItemView {
    }

    interface CollectionView {
    }
}