define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/user-list-widget-item-template.html'));

    var Translator = require( 'translator' );
    var translator = new Translator( {
        title: "Users"
    } );

    return Backbone.View.extend({

        initialize: function( options ) {
            this.count = options.count;
            this.user = options.userListItem.user;
            this.betsCount = options.userListItem.betsCount;
            this.currentUser = options.currentUser;
            this.render();
        },

        render: function () {
            var data = _.extend( {}, {
                user: this.user
                , count: this.count
                , currentUser: this.currentUser
                , translator: translator
            } );
            this.$el.html(template(data));
        }
    });
});