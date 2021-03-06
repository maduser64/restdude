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
package com.restdude.domain.users.repository;

import com.restdude.mdd.repository.ModelRepository;
import com.restdude.domain.users.model.UserRegistrationCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface UserRegistrationCodeRepository extends ModelRepository<UserRegistrationCode, String> {

    @Query("select case when (count(regCode) > 0) then true else false end  from UserRegistrationCode regCode where regCode.pk = ?1  and regCode.available = true")
    Boolean isAvailable(Serializable id);

}
