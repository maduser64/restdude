/*
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
define(["lib/restdudelib/util", 'underscore', 'handlebars', 'moment', 'backbone', 'marionette',], function (Restdude, _, Handlebars, moment, Backbone, BackboneMarionette) {

    //
    Restdude.util.Session = Backbone.Model.extend({

        initialize: function (options) {
            Backbone.Model.prototype.initialize.apply(this, arguments);
            var _session = this;
            this.userDetails = Restdude.model.UserDetailsModel.create();
        },
        getRoles: function () {
            var roles = this.isAuthenticated() ? this.userDetails.get("roles") : [];
            return roles;
        },
        // Returns true if the user is authenticated.
        isAuthenticated: function () {
            var is = this.userDetails && this.userDetails.get && this.userDetails.get(Restdude.config.idAttribute) && this.userDetails.get("active");
            return is;
        },
        ensureLoggedIn: function () {
            if (!this.isAuthenticated()) {
                // TODO: save FW to redirect after loggingin
                //				this.fw = "/" + routeHelper.mainRoutePart;
                //				// we do not need the Search suffix in the route path to match
                //				if (routeHelper.contentNavTabName != "Search") {
                //					this.fw += "/" + routeHelper.contentNavTabName;
                //					// TODO: note HTTP params
                //				}
                Restdude.navigate("login", {
                    trigger: true
                });
            }
        },

        // Attempt to load the user using the "remember me" cookie token, if any
        // exists.
        // The cookie should not be accessible by js. Here we let the server pick
        // it up
        // by itself and return the user details if appropriate
        start: function () {
            var _self = this;
            // Backbone.methodOverride = true;
            this.userDetails.fetch({
                //async : false,
                reset: true,

            });

        },
        // Logout the user here and on the server side.
        logout: function () {
            var url = this.userDetails.url();
            var _self = this;
            this.userDetails.destroy({
                url: url,
                success: function (model, response, options) {


                    Restdude.navigate("/", {
                        trigger: false
                    });
                    window.location.reload();
                },
                error: function (model, response, options) {
                    console.log("Restdude.session.logout error");

                    Restdude.navigate("/", {
                        trigger: false
                    });
                    window.location.reload();
                },


            });
        },
        getBaseUrl: function () {
            return Restdude.getBaseUrl();
        }

    });

});
