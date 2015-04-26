define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var template = _.template( require( 'text!./templates/teams-stands-off-template.html' ) );

	var MatchesModel = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-model' );
	var MatchesAndBetsCompactView = require( 'js/widgets/matches-and-bets-compact/matches-and-bets-compact-vew' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams standoff history"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cups = options.options.cups;
			this.team1 = options.options.team1;
			this.team2 = options.options.team2;

			this.score1 = options.options.score1;
			this.score2 = options.options.score2;

			this.currentUser = options.options.currentUser;

			this.render();
		},

		render: function() {
			var data = _.extend( {}, { team1: this.team1, team2: this.team2, score1: this.score1, score2: this.score2, translator: translator } );
			this.$el.html( template( data ) );

			this._renderMatches();
		},

		_renderMatches: function() {

			var el = this.$( '.js-teams-stands-off-matches' );

			var currentUser = this.currentUser;

			var model = new MatchesModel.MatchesModel();
			var view = new MatchesAndBetsCompactView( {
				model: model
				, el: el
				, settings: {
					userId: 0
					, categoryId: 0
					, cupId: 0
					, teamId: this.team1.teamId
					, team2Id: this.team2.teamId
					, showFinished: true
					, showFutureMatches: false
				}
				, menuItems: []
				, currentUser: currentUser
			} );
		}
	});
} );