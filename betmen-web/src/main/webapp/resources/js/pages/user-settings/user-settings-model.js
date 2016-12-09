define(function (require) {

    'use strict';

    var Backbone = require('backbone');

    return Backbone.Model.extend({

        initialize: function (options) {
            this.userId = options.options.userId;
        },

        url: function () {
            return '/rest/users/settings/';
        }
    });
});

