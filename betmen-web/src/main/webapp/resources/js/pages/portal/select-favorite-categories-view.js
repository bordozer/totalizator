define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var templateNoFavCategories = _.template(require('text!./templates/define-favorite-categories-template.html'));

    var service = require( '/resources/js/services/service.js' );
    var FavoriteCategoryButtonView = require( 'js/components/favorite-category-button/favorite-category-button-view' );

    var Translator = require('translator');
    var translator = new Translator({
        noFavoriteCategoriesLabel: 'You do not have favorite categories track to'
        , goAndCreateFavoriteCategoriesLabel: 'Define favorite categories'
        , favoriteCategoriesDefinedLabel: 'Favorite categories defined'
    });

    return Backbone.View.extend({

        events: {
            'click .js-favorite-categories-defined': '_favoriteCategoriesDefined'
        },

        initialize: function (options) {
            this.render();
        },

        render: function () {
            var items = service.loadCategoriesBySport();
            this.$el.html(templateNoFavCategories({
                items: items
                , translator: translator
            }));

            var container = this;
            _.each(items, function (sportItem) {
                _.each(sportItem.categories, function (category) {
                    var el = container.$(".js-pp-category-" + category.categoryId);
                    new FavoriteCategoryButtonView({el: el, category: category});
                });
            });
        },

        _favoriteCategoriesDefined: function() {
            window.location.reload();
        }
    });
});