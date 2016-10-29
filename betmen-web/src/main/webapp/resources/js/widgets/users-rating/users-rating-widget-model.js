define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');

    return Backbone.Collection.extend({

        defaults: {},

        initialize: function (options) {
            this.dateFrom = options.options.onDate;
            this.dateTo = options.options.onDateTo;
        },

        url: function () {
            return '/rest/users/rating/?dateFrom=' + this.dateFrom + '&dateTo=' + this.dateTo;
        },

        refresh: function () {
            this.fetch({cache: false});
        }
    });
});