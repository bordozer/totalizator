define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-card-template.html' ) );

	var CupsNaviView = require( 'js/components/cups-navi/cups-navi' );

	var MatchesModel = require( 'js/components/matches-and-bets/matches-and-bets-model' );
	var MatchesView = require( 'js/components/matches-and-bets/matches-and-bets-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userStatisticsLabel: "User card: Statistics"
		, userBetsLabel: "User card: User bets"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.userId = options.options.userId;

			this.render();
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			} ) );

			this._renderNavigation();

			this._renderUserStatistics();

			this._renderUserBets();
		},

		_renderNavigation: function() {
			var selectedCupId = 0;
			var cupsNaviView = new CupsNaviView( selectedCupId, this.$( '.js-cups-navi' ) );
		},

		_renderUserStatistics: function() {
			this.$( '.js-user-statistics' ).html( "User's statistics are going to be here..." );
		},

		_renderUserBets: function() {

			var el = this.$( '.js-user-bets' );

			var container = $( '<div class="col-lg-5"></div>' );
			el.append( container );

			var model = new MatchesModel.MatchesModel();
			// TODO: SET CORRECT CATEGORY/CUP/TEAM
			var view = new MatchesView.MatchesView( {
				model: model
				, el: container
				, settings: {
					userId: this.userId
					, categoryId: 1
					, cupId: 2
					, teamId: 0
					, showFutureMatches: false
					, showFinished: true
				}
				, menuItems: []
			} );
		}
	});
} );
