define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var bootbox = require('bootbox');
    var app = require('app');

    var template = _.template(require('text!./templates/match-message-item-template.html'));

    var dateTimeService = require('/resources/js/services/date-time-service.js');
    var menu = require('js/components/main-menu/main-menu');

    var Translator = require('translator');
    var translator = new Translator({
        title: "Match messages"
        , addMatchMessageLabel: "Add match message"
        , noMatchMessageTitle: "Validation error"
        , noMatchMessageAlert: "Please, define your message first"
        , deleteMatchMessageLabel: "Delete"
        , smilesLabel: "Smiles"
    });

    var SMILES = [
        {css: 'fa fa-smile-o', text: 'smile-happy'}
        , {css: 'fa fa-meh-o', text: 'smile-hmick'}
        , {css: 'fa fa-bomb', text: 'smile-bomb'}
        , {css: 'fa fa-thermometer-empty', text: 'smile-cold'}
        , {css: 'fa fa-thermometer-full', text: 'smile-hot'}
        , {css: 'fa fa-birthday-cake', text: 'smile-cake'}
        , {css: 'fa fa-balance-scale', text: 'smile-balance'}
        , {css: 'fa fa-glass', text: 'smile-drink'}
        , {css: 'fa fa-money', text: 'smile-money'}
    ];

    return Backbone.View.extend({

        events: {
            "click .js-delete-match-message": "_onDeleteMatchMessageClick"
        },

        initialize: function (options) {
            this.render();
        },

        render: function () {
            this.$el.html(template({
                matchMessage: this.model.toJSON()
                , dateTimeService: dateTimeService
                , currentUser: app.currentUser()
                , translator: translator
            }));
            //this._renderMenu(); // TODO: finish smile implementation and uncomment this
        },

        _renderMenu: function () {
            var menuItems = [];
            _.each(SMILES, function(smile) {
                menuItems.push({selector: 'js-smile-icon', icon: smile.css, link: '#', text: '', entity_id: smile.css});
            });

            var options = {
                menus: menuItems
                , menuButtonIcon: 'fa fa-smile-o'
                , menuButtonText: ''
                , menuButtonHint: translator.smilesLabel
                , cssClass: 'btn-default'
            };
            menu(options, this.$('.js-menu-smiles'));
        },

        _onDeleteMatchMessageClick: function () {
            if (!confirm(translator.deleteMatchMessageLabel + '?')) {
                return;
            }

            var self = this;
            var options = {
                success: function (model, response) {
                    self.remove();
                }
            };

            this.model.destroy(options);
        }
    });
});