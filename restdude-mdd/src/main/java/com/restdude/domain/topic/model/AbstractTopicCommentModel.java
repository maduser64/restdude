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
package com.restdude.domain.topic.model;

import com.restdude.domain.users.model.User;
import com.restdude.mdd.model.CommentModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Base topic comment impl
 */
@MappedSuperclass
public abstract class AbstractTopicCommentModel<T extends AbstractTopicModel<C>, C extends AbstractTopicCommentModel<T, C>> extends AbstractCommentableModel<C> implements CommentModel<String, T, C, User> {


    @NotNull
    @ApiModelProperty(value = "The comment text", required = true, notes = "Max byte length: " + DEFAULT_MAX_CONTENT_LENGTH)
    @Column(name = "text_content", nullable = false, updatable = false, length = DEFAULT_MAX_CONTENT_LENGTH)
    @Getter
    @Setter
    private String content;

}
