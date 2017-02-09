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
package com.restdude.mdd.controller;


import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.base.service.ModelService;
import com.restdude.domain.metadata.model.MetadatumDTO;
import com.restdude.domain.base.annotation.model.ModelDrivenPreAuth;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

public abstract class AbstractModelWithMetadataController<T extends CalipsoPersistable<PK>, PK extends Serializable, S extends ModelService<T, PK>>
        extends AbstractModelController<T, PK, S> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelWithMetadataController.class);
 

	@RequestMapping(value = "{subjectId}/metadata", method = RequestMethod.PUT)
    @ApiOperation(value = "Add metadatum", notes = "Add or updated a resource metadatum")
    @ModelDrivenPreAuth
    public void addMetadatum(@PathVariable PK subjectId,
                             @RequestBody MetadatumDTO dto) {
		service.addMetadatum(subjectId, dto);
	}

	@RequestMapping(value = "{subjectId}/metadata/{predicate}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove metadatum", notes = "Remove a resource metadatum if it exists")
    @ModelDrivenPreAuth
    public void removeMetadatum(@PathVariable PK subjectId,
                                @PathVariable String predicate) {
		service.removeMetadatum(subjectId, predicate);
	}

}
