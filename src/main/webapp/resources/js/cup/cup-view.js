define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-template.html' ) );

	var MatchesModel = require( 'js/matches/matches-model' );
	var MatchesView = require( 'js/matches/matches-view' );

	var CupsNaviView = require( 'js/components/cups-navi/cups-navi' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Menu: Admin"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var CupPageView = Backbone.View.extend( {

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			 } ) );

			var cupsNaviView = new CupsNaviView( this._getCup().cupId, this.$( '.js-cups-navi' ) );

			this._renderCupMatches();

			return this;
		},

		_renderCupMatches: function() {

			var el = this.$( '.js-cup-matches-and-bets' );

			var container = $( '<div></div>' );
			el.append( container );

			var cup = this._getCup();

			var matchesModel = new MatchesModel.MatchesModel();

			var matchesView = new MatchesView.MatchesView( {
				model: matchesModel
				, el: container
				, settings: {
					categoryId: cup.categoryId
					, cupId: cup.cupId
					, teamId: 0
				}
				, menuItems: []
			} );
		},

		_getCup: function() {
			return this.model.get( 'cup' );
		}
	} );

	return { CupPageView: CupPageView };
} );

