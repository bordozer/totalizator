define(function (require) {

    'use strict';

    var Backbone = require('backbone');

    var TeamModel = Backbone.Model.extend({
        idAttribute: 'teamId',
        defaults: {
            teamId: 0
            , teamName: ''
            , categoryId: 0
            , isEditState: false
            , teamChecked: true
            , matchCount: 0
            , teamImportId: ''
        },

        initialize: function (options) {
        },

        url: function () {
            return '/admin/rest/teams/' + this.get('teamId');
        },

        setEditState: function () {
            this.set({isEditState: true});
        },

        cancelEditState: function () {
            this.set({isEditState: false});
        }
    });

    return Backbone.Collection.extend({

        model: TeamModel,
        selectedCup: {cupId: 0},
        filterByCategory: 0,

        initialize: function (options) {
        },

        url: function () {
            return '/admin/rest/teams/categories/' + this.filterByCategory + '/';
        },

        refresh: function () {
            this.fetch({cache: false, reset: true});
        }
    });
});
