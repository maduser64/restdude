/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-tests-integration, https://manosbatsis.github.io/restdude/restdude-tests-integration
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
package com.restdude.test.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Generates static swagger docs
 */
@Test(singleThreaded = true, description = "Swagger documentation test")
public class SwaggerStaticExporterIT extends com.restdude.test.SwaggerStaticExporterIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerStaticExporterIT.class);

}