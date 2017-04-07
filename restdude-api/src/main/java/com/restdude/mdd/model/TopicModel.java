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
package com.restdude.mdd.model;

import java.io.Serializable;

/**
 * Base interface for topics
 */
public interface TopicModel<PK extends Serializable, C extends CommentModel<?, ?, C, U>, U extends UserModel> extends GeneratedContentModel<PK, U>, CommentableModel<C> {

    public static final int DEFAULT_MAX_DESCRIPTION_LENGTH = 1000;
    public static final int MAX_MSTACKTRACE_LENGTH = 40000;
    public static final int MAX_MESSAGE_LENGTH = 500;
}
