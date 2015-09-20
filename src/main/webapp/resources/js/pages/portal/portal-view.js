define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/portal-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var app = require( 'app' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Administration"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;

	var PortalPageView = Backbone.View.extend( {

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			 } ) );

			this._renderMatchesOnDate();

			this._renderMatches();

			return this;
		},

		_renderMatchesOnDate: function () {

			var model = this.model.toJSON();
			console.log( model.cupsTodayToShow );

			var currentUser = app.currentUser();

			var container = this.$( '.portal-template.html' );

			_.each( model.cupsTodayToShow, function( cup ) {

				var el = $( '<div class="col-xs-12"></div>' );
				container.append( el );

				var options = {
					filter: {
						categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFutureMatches: true
						, showFinished: true
					}
					, matchViewMode: VIEW_MODE_MINIMIZED
					, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_MATCHES
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			} );
		},

		_renderMatches: function() {

			var model = this.model.toJSON();

			var currentUser = app.currentUser();

			var container = this.$( '.js-portal-page-container' );

			_.each( model.cupsToShow, function( cup ) {

				var el = $( '<div class="col-xs-12 col-lg-6"></div>' );
				container.append( el );

				var options = {
					filter: {
						categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFutureMatches: true
					}
					, matchViewMode: VIEW_MODE_MINIMIZED
					, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_STATISTICS
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			} );
		}
	} );

	return { PortalPageView: PortalPageView };
} );

