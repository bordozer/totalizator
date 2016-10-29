define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/favorite-category-button-template.html' ) );

	var favoritesService = require( '/resources/js/services/favorites-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		addOrRemoveHint: "Add/Remove the category to/from Favorites"
		, addToFavoritesConfirmation: "Add to favorites this category?"
		, removeFromFavoritesConfirmation: "Remove from favorites this category?"
	} );

	var CATEGORY_IS_IN_FAVORITE_CLASSES = 'btn-danger js-remove-from-favorites-button';
	var CATEGORY_IS_NOT_IN_FAVORITE_CLASSES = 'js-add-to-favorites-button';

	return Backbone.View.extend( {

		events: {
			'click .js-add-to-favorites-button': '_addToFavorites'
			, 'click .js-remove-from-favorites-button': '_removeFromFavorites'
		},

		initialize: function ( options ) {
			this.category = options.category;

			this.render();
		},

		render: function () {

			var data = _.extend( {}, {
				category: this.category
				, favoriteIconClasses: this.category.favoriteCategory ? CATEGORY_IS_IN_FAVORITE_CLASSES : CATEGORY_IS_NOT_IN_FAVORITE_CLASSES
				, translator: translator
			} );

			this.$el.html( template( data ) );
		},

		_addToFavorites: function( evt ) {

			if ( ! confirm( translator.addToFavoritesConfirmation ) ) {
				return;
			}

			var categoryId = $( evt.target ).data( 'category_id' );

			favoritesService.addToCategoryToFavoritesOfCurrentUser( categoryId );

			this.$( '.js-favorite-category-button' ).removeClass( CATEGORY_IS_NOT_IN_FAVORITE_CLASSES );
			this.$( '.js-favorite-category-button' ).addClass( CATEGORY_IS_IN_FAVORITE_CLASSES );
		},

		_removeFromFavorites: function( evt ) {

			if ( ! confirm( translator.removeFromFavoritesConfirmation ) ) {
				return;
			}

			var categoryId = $( evt.target ).data( 'category_id' );

			favoritesService.removeCategoryFromFavoritesOfCurrentUser( categoryId );

			this.$( '.js-favorite-category-button' ).removeClass( CATEGORY_IS_IN_FAVORITE_CLASSES );
			this.$( '.js-favorite-category-button' ).addClass( CATEGORY_IS_NOT_IN_FAVORITE_CLASSES );
		}
	} );
} );