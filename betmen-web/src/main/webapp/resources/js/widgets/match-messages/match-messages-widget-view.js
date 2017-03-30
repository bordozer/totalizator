define(function (require) {

    'use strict';

    var _ = require('underscore');
    var $ = require('jquery');
    var bootbox = require('bootbox');
    var app = require('app');

    var templateList = _.template(require('text!./templates/match-messages-template.html'));
    var MatchMessageModel = require('./match-messages-widget-model');
    var MatchMessagesItemView = require('./match-message-item-view');

    var WidgetView = require('js/components/widget/widget-view');

    var dateTimeService = require('/resources/js/services/date-time-service.js');

    var Translator = require('translator');
    var translator = new Translator({
        title: "Match messages"
        , addMatchMessageLabel: "Add match message"
        , noMatchMessageTitle: "Validation error"
        , noMatchMessageAlert: "Please, define your message first"
        , deleteMatchMessageLabel: "Delete"
    });

    return WidgetView.extend({

        events: {
            "click .js-add-match-message": "_onAddMatchMessageClick"
        },

        initialize: function (options) {
            this.listenTo(this.model, 'sync', this._renderMatchMessages);
            this.render();
        },

        renderBody: function () {
            this.model.fetch({cache: false});
        },

        getTitle: function () {
            return translator.title;
        },

        getIcon: function () {
            return 'fa fa-file-text-o';
        },

        getCustomMenuItems: function () {
            return [];
        },

        _renderMatchMessages: function () {
            var container = this.$(this.windowBodyContainerSelector);
            container.html('');

            var matchId = this.model.matchId;
            var self = this;
            this.model.forEach(function (model) {
                self._renderMatchMessage(matchId, model);
            });

            container.append(templateList({
                translator: translator
            }));

            this.trigger('inner-view-rendered');
        },

        _renderMatchMessage: function (matchId, model) {
            var container = this.$(this.windowBodyContainerSelector);
            var el = $("<div></div>");
            container.append(el);

            var view = new MatchMessagesItemView({model: model, el: el, options: {matchId: matchId}});
        },

        _onAddMatchMessageClick: function () {
            var matchMessageText = this.getMatchMessage();
            if (matchMessageText.length == 0) {
                bootbox.dialog({
                    title: translator.noMatchMessageTitle
                    , message: translator.noMatchMessageAlert
                });
                return;
            }

            var rend = _.bind(function () {
                this.render();
            }, this);

            var matchMessage = new MatchMessageModel.MatchMessageModel({matchId: this.model.matchId, messageText: matchMessageText});
            matchMessage.save().then(rend);
        },

        getMatchMessage: function () {
            return this.$("[name='matchMessageText']").val().trim();
        }
    });
});
