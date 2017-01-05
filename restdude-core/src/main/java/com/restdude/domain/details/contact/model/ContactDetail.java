/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.details.contact.model;

import com.restdude.domain.base.model.CalipsoPersistable;

import java.io.Serializable;

public interface ContactDetail<ID extends Serializable> extends CalipsoPersistable<ID> {

    String getValue();

    Boolean getVerified();

    void setVerified(Boolean verified);

    Boolean getPrimary();

    void setPrimary(Boolean primary);

    ContactDetails getContactDetails();

    void setContactDetails(ContactDetails contactDetails);

    String getVerificationToken();

    void setVerificationToken(String verificationToken);
}
