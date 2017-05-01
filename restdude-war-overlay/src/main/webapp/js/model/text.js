/*
 * Copyright (c) 2007 - 2016 Manos Batsis
 *
 * This file is part of Restdude, a software platform by www.Abiss.gr.
 *
 * Restdude is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Restdude is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Restdude. If not, see http://www.gnu.org/licenses/agpl.html
 */
define(['restdude', 'model/resource'],
    function (Restdude, ResourceModel) {
        //Backbone.Form.editors.Markdown
        var TextModel = ResourceModel.extend({
                modelKey: "text",
            },
            // static members
            {
                parent: ResourceModel,
                showInMenu: true,
                label: "Text"
            });

        /**
         * Get the model class URL fragment corresponding this class
         * @returns the URL path fragment as a string
         */
        TextModel.prototype.getPathFragment = function () {
            return "texts";
        }
        TextModel.prototype.getFormSchemas = function () {
            // superclass schema
            var superSchema = TextModel.parent.getFormSchemas();
            // own schema
            var schema = {
                "sourceContentType": {
                    "search": {type: 'Select', options: ['text/plain', 'text/x-markdown', 'text/html']},
                    "default": {
                        type: 'Select',
                        options: ['text/plain', 'text/x-markdown', 'text/html'],
                        validators: ['required']
                    }
                },
                "source": {
                    "search": 'Text',
                    "default": {
                        type: BackboneFormMarkdown,
                        validators: ['required'],
                        editorAttrs: {"data-provide": 'markdown'}
                    },
                }

            };
            // return merged schemas
            return $.extend({}, superSchema, schema);
        }

        TextModel.prototype.getGridSchema = function () {
            var superSchema = ResourceModel.prototype.getGridSchema();
            var localSchema = [/*{
             source : "source",
             label : "source",
             cell : "string"
             }*/];
            // return merged schemas
            return $.extend({}, superSchema, localSchema);
        }
        return TextModel;
    });