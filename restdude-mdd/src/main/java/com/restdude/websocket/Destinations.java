/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-mdd, https://manosbatsis.github.io/restdude/restdude-mdd
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
package com.restdude.websocket;

import com.restdude.websocket.message.IActivityNotificationMessage;

public class Destinations {

    /**
     * Publishes {@link com.restdude.websocket.message.IStateUpdateMessage} state updates for entities the user is expected to be interested in, for example friend status
     */
    public final static String USERQUEUE_UPDATES_STATE = "/queue/updates/state";

    /**
     * Publishes {@link IActivityNotificationMessage} activity updates of entities the user is expected to be interested in
     */
    public final static String USERQUEUE_UPDATES_ACTIVITY = "/queue/updates/activity";

    public static final String USERQUEUE_FRIENDSHIPS = "/queue/friendships";

}
