/**
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-error, https://manosbatsis.github.io/restdude/restdude-error
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
package com.restdude.domain.error.service;

import com.restdude.domain.base.service.AbstractAssignedIdModelService;
import com.restdude.domain.error.model.UserAgent;


public interface UserAgentService extends AbstractAssignedIdModelService<UserAgent, String> {
    public static final String BEAN_ID = "userAgentService";
}