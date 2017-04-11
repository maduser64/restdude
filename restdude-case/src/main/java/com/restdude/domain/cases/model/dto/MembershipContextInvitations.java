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
package com.restdude.domain.cases.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ApiModel(description = "A model representing an invitation by a MembershipContext owner, addressed to multiple users.")
public class MembershipContextInvitations implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The ID of the MembershipContext this invitation list applies to. ")
	private String membershipContextId;

	@ApiModelProperty(value = "The carbon copy recipients list, where each entry is a user id, a username or an email address. Used only by MembershipContext  owners to create multiple invitations in a single request. Holds the invitations sent successfully when model is a the body of an HTTP response.")
	private Set<String> cc;

	public MembershipContextInvitations() {
		super();
	}

	public String getMembershipContextId() {
		return membershipContextId;
	}

	public void setMembershipContextId(String membershipContextId) {
		this.membershipContextId = membershipContextId;
	}

	public Set<String> getCc() {
		return cc;
	}

	public void setCc(Set<String> cc) {
		this.cc = cc;
	}

	public static class Builder {
		private String businessContextId;
		private Set<String> cc;

		public Builder businessContextId(String businessContextId) {
			this.businessContextId = businessContextId;
			return this;
		}

		public Builder cc(Set<String> cc) {
			this.cc = cc;
			return this;
		}
		
		public Builder cc(String ccItem) {
			if(this.cc == null){
				this.cc = new HashSet<String>();
			}
			this.cc.add(ccItem);
			return this;
		}

		public MembershipContextInvitations build() {
			return new MembershipContextInvitations(this);
		}
	}

	private MembershipContextInvitations(Builder builder) {
		this.membershipContextId = builder.businessContextId;
		this.cc = builder.cc;
	}
}
