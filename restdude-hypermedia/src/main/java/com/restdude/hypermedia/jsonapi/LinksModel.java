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
package com.restdude.hypermedia.jsonapi;

import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.List;

public interface LinksModel extends Serializable {


    void add(Link link);

    void add(Iterable<Link> links);

    void add(Link... links);

    boolean hasLinks();

    boolean hasLink(String rel);

    List<Link> getLinks();

    void removeLinks();

    Link getLink(String rel);
}