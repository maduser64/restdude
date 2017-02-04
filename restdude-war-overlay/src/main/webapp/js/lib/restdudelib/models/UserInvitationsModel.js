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
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/restdudelib/util", "lib/restdudelib/form",
        "lib/restdudelib/uifield", "lib/restdudelib/backgrid", "lib/restdudelib/view", 'handlebars', "lib/restdudelib/models/Model", "lib/restdudelib/models/UserModel"],
    function ($, _, Bloodhoud, Typeahead, Restdude, RestdudeForm, RestdudeField, RestdudeGrid, RestdudeView, Handlebars, Model, UserModel) {

        var RecipientModel = Backbone.Model.extend({
            initialize: function () {
                Backbone.Model.prototype.initialize.apply(this, arguments);
                this.set("@class", "gr.abiss.restdude.model.model.UserDTO");
            },
            schema: {
                "@class": {
                    type: Restdude.backboneform.ClassName,
                    defaultValue: "gr.abiss.restdude.model.model.UserDTO",
                },
                name: {
                    type: 'Text',
                },
                email: {
                    type: 'Text',
                    validators: ['required', 'email']
                },
            },
            //To string is how models in the list will appear in the "editor".
            toString: function () {
                return this.attributes.name + '&lt;' + this.attributes.email + '&gt;';
            }
        });


        Restdude.model.UserInvitationsModel = Restdude.Model.extend(
            /** @lends Restdude.model.RoleModel.prototype */
            {
                toString: function () {
                    return this.get("name");
                }
            }, {
                // static members
                labelIcon: "fa fa-envelope-o fa-fw",
                pathFragment: "invitations",
                typeName: "Restdude.model.UserInvitationsModel",
                menuConfig: null,
                useCases: {
                    create: {
                        view: Restdude.view.UserInvitationsLayout,
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Restdude.getTemplate("UseCaseCardFormView"),
                                }
                            }
                        }
                    }
                },
                fields: function () {
                    return {
                        addressLines: {
                            fieldType: "Text",
                        },
                        recepients: {
                            fieldType: "Lov",
                            form: {
                                type: 'List',
                                itemType: 'NestedModel',
                                model: RecipientModel
                            }
                        },
                    };
                },
            });


    });