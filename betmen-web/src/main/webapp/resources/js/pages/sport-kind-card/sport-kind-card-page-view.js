define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var app = require( 'app' );

	var template = _.template( require( 'text!./templates/sport-kind-card-page-template.html' ) );

	var cupWinners = require( 'js/widgets/cup-winners/cup-winners-widget' );
	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var FavoriteCategoryButtonView = require( 'js/components/favorite-category-button/favorite-category-button-view' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;

	return Backbone.View.extend( {

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

				var el = $( "<div class='row'><div class='col-12'></div></div>" );
				container.append( el );

				var data = _.extend( {}, {
					category: category
				} );

				el.html( template( data ) );

				_.each( model.cups, function ( cup ) {

					var isCupFinished = cup.finished;

					// var el_w = $( "<div class='col-" + ( isCupFinished ? '3' : '4' ) + "'></div>" );
					var el_w = $( "<div class='col-4'></div>" );
					el.append( el_w );

					if ( isCupFinished ) {
						self._renderFinishedCup( el_w, cup );
					} else {
						self._renderActiveCup( el_w, cup );
					}
				} );

				self._renderFavoriteCategoryButton( category, el );
			} );
		},

		_renderFavoriteCategoryButton: function ( category, el ) {
			new FavoriteCategoryButtonView( { el: $( '.js-favorite-category-button', el ), category: category } );
		},

		_renderActiveCup: function ( el, cup ) {

			var options = {
				filter: {
					categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, showFutureMatches: true
					, filterByDate: dateTimeService.dateNow()
					, sorting: 2
				}
				, matchViewMode: VIEW_MODE_MINIMIZED
				, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_STATISTICS
				, currentUser: app.currentUser()
			};
			matchesAndBetsView( el, options );
		},

		_renderFinishedCup: function ( el, cup ) {
			cupWinners( el, { cup: cup } );
		}
	} );
} );