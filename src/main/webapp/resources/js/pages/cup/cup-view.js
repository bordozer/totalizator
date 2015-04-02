define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-template.html' ) );

	var MatchesModel = require( 'js/components/matches-and-bets/matches-and-bets-model' );
	var MatchesView = require( 'js/components/matches-and-bets/matches-and-bets-view' );

	var CupScoresModel = require( 'js/components/cup-users-scores/cup-users-scores-model' );
	var CupScoresView = require( 'js/components/cup-users-scores/cup-users-scores-view' );

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

			this._renderNavigation();

			this._renderCupScores();

			this._renderCupMatches();

			return this;
		},

		_renderNavigation: function() {
			var view = new CupsNaviView( this._getCup().cupId, this.$( '.js-cups-navi' ) );
		},

		_renderCupScores: function() {
			var el = this.$( '.js-cup-scores' );

			var cup = this._getCup();

			var model = new CupScoresModel( { cupId: cup.cupId } );

			var view = new CupScoresView( {
				model: model
				, el: el
			} );
		},

		_renderCupMatches: function() {

			var el = this.$( '.js-cup-matches-and-bets' );

			var container = $( '<div></div>' );
			el.append( container );

			var cup = this._getCup();

			var model = new MatchesModel.MatchesModel();

			var view = new MatchesView.MatchesView( {
				model: model
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
			return this.model.toJSON();
		}
	} );

	return { CupPageView: CupPageView };
} );
