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

//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

//@JsonDeserialize(/*as=UserDetails.class*/)
public interface UserDetails extends SocialUserDetails, Principal, PersistableModel<String> {


    public String getPk();

    public void setPk(String id);

    public String getFirstName();

	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName);

	public String getName();

	public void setName(String name);

    public LocalDateTime getLastPassWordChangeDate();

    public void setLastPassWordChangeDate(LocalDateTime lastPassWordChangeDate);

	public String getEmailHash();

	public void setEmailHash(String emailHash);

	public String getAvatarUrl();

	public void setAvatarUrl(String avatarUrl);

    public LocalDate getBirthDay();

    public void setBirthDay(LocalDate birthDay);

    public LocalDateTime getLastVisit();

    public void setLastVisit(LocalDateTime lastVisit);

    public LocalDateTime getLastPost();

    public void setLastPost(LocalDateTime lastPost);

	public Short getLoginAttempts();

	public void setLoginAttempts(Short loginAttempts);

	public Boolean getActive();

	public void setActive(Boolean active);

	public String getInactivationReason();

	public void setInactivationReason(String inactivationReason);

    public LocalDateTime getInactivationDate();

    public void setInactivationDate(LocalDateTime inactivationDate);

	public String getLocale();

	public void setLocale(String locale);

	public String getDateFormat();

	public void setDateFormat(String dateFormat);

	public boolean isAdmin();

	public void setAdmin(boolean isAdmin);

	public boolean isSiteAdmin();

	public void setSiteAdmin(boolean isSiteAdmin);

	public void setRedirectUrl(String redirectUrl);

	public String getRedirectUrl();

	public Map<String, String> getMetadata();

	public void setMetadata(Map<String, String> metadata);

	public void addMetadatum(String predicate, String object);

	public void setUsername(String username);

	public String getPassword();
	public void setPassword(String password);

	public void setAuthorities(List<? extends GrantedAuthority> authorities);

	void setCellphone(String cellphone);

	String getCellphone();

	void setTelephone(String telephone);

	String getTelephone();

    public boolean isNew();

}