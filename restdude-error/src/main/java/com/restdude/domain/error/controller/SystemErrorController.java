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
package com.restdude.domain.error.controller;

import com.restdude.domain.cases.model.dto.CaseCommenttInfo;
import com.restdude.domain.error.model.SystemError;
import com.restdude.domain.error.service.SystemErrorService;
import com.restdude.mdd.controller.AbstractPersistableModelController;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/api/rest/"+SystemError.API_PATH)
public class SystemErrorController extends AbstractPersistableModelController<SystemError, String, SystemErrorService> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemError create(@RequestBody SystemError resource) {
        resource.addRequestInfo(this.request);
        return this.service.create(resource);
    }

    @ApiOperation(value = "Update files",
            notes = "The files are saved using the parameter names of the multipart files contained in this request. "
                    + "These are the field names of the form (like with normal parameters), not the original file names.")
    @RequestMapping(value = "{id}/files",
            method = {RequestMethod.POST},
            headers = ("content-type=multipart/*"),
            produces = {"application/json", "application/xml"})
    public SystemError updateFiles(@PathVariable String id,
                                   MultipartHttpServletRequest request, HttpServletResponse response) {
        return this.service.updateFiles(id, request, response);
    }

    /**
     * Utility method to be called by implementations
     *
     * @param id
     * @param filenames
     */
    public void deleteFiles(String id, String... filenames) {
        this.service.deleteFiles(id, filenames);
        ;
    }

    @ApiOperation(value = "Get all comments for the case matching the given id (compact)")
    @RequestMapping(value = "{id}/comments",
            method = {RequestMethod.GET},
            produces = {"application/json"})
    public List<CaseCommenttInfo> getComments(@PathVariable String id) {
        return this.service.getCompactCommentsBySubject(id);
    }

}
