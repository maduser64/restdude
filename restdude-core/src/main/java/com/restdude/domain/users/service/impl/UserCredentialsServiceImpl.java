/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.users.service.impl;

import com.restdude.domain.base.service.impl.AbstractModelServiceImpl;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.repository.UserCredentialsRepository;
import com.restdude.domain.users.service.UserCredentialsService;

import javax.inject.Named;


@Named("userCredentialsService")
public class UserCredentialsServiceImpl extends AbstractModelServiceImpl<UserCredentials, String, UserCredentialsRepository> implements UserCredentialsService {

}