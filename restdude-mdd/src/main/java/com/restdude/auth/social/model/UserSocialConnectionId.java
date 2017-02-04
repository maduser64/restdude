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
package com.restdude.auth.social.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class UserSocialConnectionId implements Serializable{

	@NotNull
	@Column(length = 255, nullable = false)
	private String userId;
	@NotNull
	@Column(length = 255, nullable = false)
	private String providerId;
	@NotNull
	@Column(length = 255, nullable = false)
	private String providerUserId;

	public UserSocialConnectionId() {

	}

	public UserSocialConnectionId(String userId, String providerId, String providerUserId) {
		super();
		this.userId = userId;
		this.providerId = providerId;
		this.providerUserId = providerUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	@Override
	public boolean equals(final Object obj) {
		if (UserSocialConnectionId.class.isAssignableFrom(obj.getClass())) {
			final UserSocialConnectionId other = (UserSocialConnectionId) obj;
			return new EqualsBuilder()
					.append(this.getUserId(), other.getUserId())
					.append(this.getProviderId(), other.getProviderId())
					.append(this.getProviderUserId(), other.getProviderUserId())
					.isEquals();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
				.append(this.getUserId())
				.append(this.getProviderId())
				.append(this.getProviderUserId())
				.toHashCode();
	}


}
