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
package com.restdude.websocket.message;

import java.io.Serializable;

/**
 * Base class for subject and object parts of a {@link IActivityNotificationMessage}
 */
public class MessageResource<PK extends Serializable> implements IMessageResource<PK> {

    /**
     * The resource ID
     */
    protected PK id;

    /**
     * The resource human-readable name
     */
    protected String name;

    public MessageResource() {
        super();
    }

    public MessageResource(PK id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public PK getPk() {
        return id;
    }

    public void setPk(PK id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
