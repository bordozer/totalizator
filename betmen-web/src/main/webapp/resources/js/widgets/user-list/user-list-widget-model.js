define(function (require) {

    'use strict';

    var Backbone = require('backbone');

    var UserModel = Backbone.Model.extend({

        defaults: {
            userId: 0
            , userName: ''
        },

        initialize: function (options) {
        },

        parse: function (response) {
            response.id = response.user.id;
            return response;
        }
    });

    return Backbone.Collection.extend({

        model: UserModel,

        initialize: function (options) {
        },

        url: function () {
            return '/rest/users/list/';
        }
    });
});
