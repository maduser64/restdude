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
package com.restdude.domain.cms.model;

import com.restdude.mdd.model.AbstractSystemUuidPersistableResource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
/**
 */
@Entity
@Table(name = "content_file")
public class File extends AbstractSystemUuidPersistableResource {

	private static final long serialVersionUID = -7942906897981646998L;


	/**
	 * The MIME type of the resource. 
	 */
    @NotNull
    @Column(name = "mime", nullable = false)
	private String mime;

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}
		if (!(obj instanceof File)) {
			return false;
		}
		File that = (File) obj;
        return null == this.getPk() ? false : this.getPk().equals(that.getPk());
    }



}