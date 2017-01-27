define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/fav-cat-stat-widget-template.html'));

    var WidgetView = require('js/components/widget/widget-view');
    var dateTimeService = require('/resources/js/services/date-time-service.js');

    var Translator = require('translator');
    var translator = new Translator({
        favoriteCategoriesStatistics: "Next day games"
        , matchesCount: "Matches count total"
        , betsCount: "Bets count"
        , noGames: "No games on date"
    });

    return WidgetView.extend({

        events: {},

        initialize: function (options) {
            this.listenTo(this.model, 'sync', this._renderStatistics);
            this.render();
        },

        renderBody: function () {
            this.model.fetch({cache: false});
        },

        getTitle: function () {
            return translator.favoriteCategoriesStatistics + ':<br /><small>' + dateTimeService.formatDateFullDisplay(this.model.onDate) + '</small>';
        },

        getIcon: function () {
            return 'fa fa-info-circle';
        },

        getCustomMenuItems: function () {
            return [];
        },

        _renderStatistics: function () {
            var jmodel = this.model.toJSON();
            if (jmodel.categoryBetStatistics.length == 0) {
                var noGames = "<span class='text-muted'>" + translator.noGames + ' <br /><strong>' + dateTimeService.formatDateFullDisplay(this.model.onDate) + "</strong></span>";
                this.setBody(noGames);
                this.trigger('inner-view-rendered');
                return;
            }
            var data = _.extend({}, jmodel, {translator: translator});
            this.setBody(template(data));

            this.trigger('inner-view-rendered');
        }
    });
});