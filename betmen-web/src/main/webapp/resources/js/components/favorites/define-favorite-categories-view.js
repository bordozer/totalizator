define(function (require) {

    'use strict';

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var templateNoFavCategories = _.template(require('text!./templates/define-favorite-categories-template.html'));

    var service = require('/resources/js/services/service.js');
    var FavoriteCategoryButtonView = require('js/components/favorite-category-button/favorite-category-button-view');

    return Backbone.View.extend({

        events: {},

        initialize: function (options) {
            this.render();
        },

        render: function () {
            var items = service.loadCategoriesBySport();
            this.$el.html(templateNoFavCategories({
                items: items
            }));

            var self = this;
            _.each(items, function (sportItem) {
                _.each(sportItem.categories, function (category) {
                    var el = self.$(".js-pp-category-" + category.categoryId);
                    new FavoriteCategoryButtonView({el: $('.js-favorite-category-button', el), category: category});
                });
            });
        }
    });
});