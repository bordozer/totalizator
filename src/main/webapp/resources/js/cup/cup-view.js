define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-template.html' ) );

	var MatchesModel = require( 'js/matches/matches-model' );
	var MatchesView = require( 'js/matches/matches-view' );

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

			this._renderCupMatches();

			return this;
		},

		_renderCupMatches: function() {

			var el = this.$( '.js-cup-matches-and-bets' );

			var container = $( '<div></div>' );
			el.append( container );

			var cup = this.model.get( 'cup' );

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
		}
	} );

	return { CupPageView: CupPageView };
} );

