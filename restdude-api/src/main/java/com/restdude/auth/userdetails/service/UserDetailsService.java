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
package com.restdude.auth.userdetails.service;

import com.restdude.auth.userAccount.model.EmailConfirmationOrPasswordResetRequest;
import com.restdude.auth.userAccount.model.UsernameChangeRequest;
import com.restdude.domain.UserDetails;
import com.restdude.domain.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.SocialUserDetailsService;


public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService, SocialUserDetailsService, ConnectionSignUp, SignInAdapter {

    UserDetails updateUsername(UsernameChangeRequest usernameChangeRequest);

    UserDetails resetPassword(EmailConfirmationOrPasswordResetRequest resource);

    UserDetails create(UserDetails tryUserDetails);

    void handlePasswordResetRequest(String userNameOrEmail);

//	ICalipsoUserDetails confirmPrincipal(String confirmationToken);

    UserDetails createForImplicitSignup(UserModel user);


    Authentication getAuthentication();

    UserDetails getPrincipal();

    UserModel getPrincipalLocalUser();

	void updateLastLogin(UserDetails u);
}