define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var template = _.template(require('text!./templates/favorites-page-template.html'));

    var DefineFavoriteCategoriesView = require( 'js/components/favorites/define-favorite-categories-view' );

    return Backbone.View.extend({

        initialize: function (options) {
            this.render();
        },

        render: function () {
            this.$el.html(template());
            this._renderFavorites();
        },

        _renderFavorites: function() {
            new DefineFavoriteCategoriesView({el: this.$('.js-favorites-categories')});
        }
    });
});