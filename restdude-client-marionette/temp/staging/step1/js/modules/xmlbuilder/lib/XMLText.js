// Generated by CoffeeScript 1.6.3
(function() {
  var XMLNode, XMLText, create,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  create = require('lodash.create');

  XMLNode = require('./XMLNode');

  module.exports = XMLText = (function(_super) {
    __extends(XMLText, _super);

    function XMLText(parent, text) {
      this.parent = parent;
      XMLText.__super__.constructor.call(this, parent);
      if (text == null) {
        throw new Error("Missing element text");
      }
      this.value = this.stringify.eleText(text);
    }

    XMLText.prototype.clone = function() {
      return create(XMLText.prototype, this);
    };

    XMLText.prototype.toString = function(options, level) {
      var indent, newline, pretty, r, space;
      pretty = (options != null ? options.pretty : void 0) || false;
      indent = (options != null ? options.indent : void 0) || '  ';
      newline = (options != null ? options.newline : void 0) || '\n';
      level || (level = 0);
      space = new Array(level + 1).join(indent);
      r = '';
      if (pretty) {
        r += space;
      }
      r += this.value;
      if (pretty) {
        r += newline;
      }
      return r;
    };

    return XMLText;

  })(XMLNode);

}).call(this);