define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/portal-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

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
			this.cupsToShow = options.options.cupsToShow;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			 } ) );

			this._renderMatches();

			return this;
		},

		_renderMatches: function() {

			var currentUser = this.currentUser;

			var el = this.$( '.js-portal-page-container' );

			_.each( this.cupsToShow, function( cup ) {

				var container = $( '<div class="col-xs-12 col-lg-4"></div>' );
				el.append( container );

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
				matchesAndBetsView( container, options );
			} );
		}
	} );

	return { PortalPageView: PortalPageView };
} );

