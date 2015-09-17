define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var app = require( 'app' );

	var template = _.template( require( 'text!./templates/sport-kind-card-page-template.html' ) );

	var cupWinners = require( 'js/widgets/cup-winners/cup-winners-widget' );
	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var favoritesService = require( '/resources/js/services/favorites-service.js' );

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;

	var Translator = require( 'translator' );
	var translator = new Translator( {
		addToFavoritesConfirmation: "Add to favorites this category?"
		, removeFromFavoritesConfirmation: "Remove from favorites this category?"
		, addOrRemoveHint: "Add/Remove the category to/from Favorites"
	} );

	var CATEGORY_IS_IN_FAVORITE_CLASSES = 'btn-danger js-remove-from-favorites-button';
	var CATEGORY_IS_NOT_IN_FAVORITE_CLASSES = 'js-add-to-favorites-button';

	return Backbone.View.extend( {

		events: {
			'click .js-add-to-favorites-button': '_addToFavorites'
			, 'click .js-remove-from-favorites-button': '_removeFromFavorites'
		},

		initialize: function ( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this._renderCupsWinners();

			return this;
		},

		_renderCupsWinners: function () {

			var container = this.$el;
			var self = this;

			this.model.forEach( function ( _model ) {

				var model = _model.toJSON();
				var category = model.category;

				var el = $( "<div></div>" );
				container.append( el );

				var data = _.extend( {}, {
					category: category
					, favoriteIconClasses: category.favoriteCategory ? CATEGORY_IS_IN_FAVORITE_CLASSES : CATEGORY_IS_NOT_IN_FAVORITE_CLASSES
					, translator: translator
				} );

				el.html( template( data ) );

				_.each( model.cups, function ( cup ) {

					var isCupFinished = cup.finished;

					var el_w = $( "<div class='col-xs-" + ( isCupFinished ? '3' : '4' ) + "'></div>" );
					el.append( el_w );

					if ( isCupFinished ) {
						self._renderFinishedCup( el_w, cup );
					} else {
						self._renderActiveCup( el_w, cup );
					}
				} );
			} );
		},

		_renderActiveCup: function ( el, cup ) {

			var options = {
				filter: {
					categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, showFutureMatches: true
				}
				, matchViewMode: VIEW_MODE_MINIMIZED
				, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_STATISTICS
				, currentUser: app.currentUser()
			};
			matchesAndBetsView( el, options );
		},

		_renderFinishedCup: function ( el, cup ) {
			cupWinners( el, { cup: cup } );
		},

		_addToFavorites: function( evt ) {

			if ( ! confirm( translator.addToFavoritesConfirmation ) ) {
				return;
			}

			var categoryId = $( evt.target ).data( 'category_id' );

			favoritesService.addToCategoryToFavoritesOfCurrentUser( categoryId );

			this.$( '.js-favorite-category-button-' + categoryId ).removeClass( CATEGORY_IS_NOT_IN_FAVORITE_CLASSES );
			this.$( '.js-favorite-category-button-' + categoryId ).addClass( CATEGORY_IS_IN_FAVORITE_CLASSES );
		},

		_removeFromFavorites: function( evt ) {

			if ( ! confirm( translator.removeFromFavoritesConfirmation ) ) {
				return;
			}

			var categoryId = $( evt.target ).data( 'category_id' );

			favoritesService.removeCategoryFromFavoritesOfCurrentUser( categoryId );

			this.$( '.js-favorite-category-button-' + categoryId ).removeClass( CATEGORY_IS_IN_FAVORITE_CLASSES );
			this.$( '.js-favorite-category-button-' + categoryId ).addClass( CATEGORY_IS_NOT_IN_FAVORITE_CLASSES );
		}
	} );
} );