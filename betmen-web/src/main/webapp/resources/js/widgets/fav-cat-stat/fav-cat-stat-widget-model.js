define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');

    return Backbone.Model.extend({

        defaults: {},

        initialize: function (options) {
            this.onDate = options.options.onDate;
        },

        url: function () {
            return '/rest/portal-page/favorites/statistics/?onDate=' + this.onDate;
        }
    });
});