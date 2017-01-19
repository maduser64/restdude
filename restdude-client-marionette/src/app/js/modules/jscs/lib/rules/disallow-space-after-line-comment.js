var assert = require('assert');

module.exports = function() {};

module.exports.prototype = {

    configure: function(disallowSpaceAfterLineComment) {
        assert(
            disallowSpaceAfterLineComment === true,
            'disallowSpaceAfterLineComment option requires the value true'
        );
    },

    getOptionName: function() {
        return 'disallowSpaceAfterLineComment';
    },

    check: function(file, errors) {
        var comments = file.getComments();

        comments.forEach(function(comment) {
            if (comment.type === 'Line') {
                var value = comment.value;
                if (value.length > 0 && value[0] === ' ') {
                    errors.add('Illegal space after line comment', comment.loc.start);
                }
            }
        });
    }
};
