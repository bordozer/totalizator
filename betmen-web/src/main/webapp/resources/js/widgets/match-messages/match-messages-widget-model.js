define(function (require) {

    'use strict';

    var Backbone = require('backbone');

    var MatchMessageModel = Backbone.Model.extend({

        idAttribute: 'id',

        defaults: {
            id: 0
        },

        initialize: function (options) {
            this.matchId = options.matchId;
        },

        url: function () {
            return '/rest/messages/matches/' + this.get('id');
        }
    });

    var MatchMessageModels = Backbone.Collection.extend({

        model: MatchMessageModel,

        initialize: function (options) {
            this.matchId = options.options.matchId;
        },

        url: function () {
            return '/rest/messages/matches/' + this.matchId + '/';
        }
    });

    return {MatchMessageModels: MatchMessageModels, MatchMessageModel: MatchMessageModel};
});