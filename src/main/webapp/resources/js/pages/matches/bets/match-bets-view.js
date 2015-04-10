define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bets-template.html' ) );

	var CupsNaviView = require( 'js/components/cups-navi/cups-navi' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches and bests"
		, userLabel: "User"
		, userPointsLabel: "Points"
	} );

	var MatchBetsView = Backbone.View.extend( {

		initialize: function ( options ) {
			this.cupId = options.options.cupId;
			this.matchId = options.options.matchId;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

//			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } ); // if I want to highlight the cup in navigation...

			var model = this.model.toJSON();

			var match = model.match;
			var matchResults = service.matchResultsByMatch( match );

			var data = _.extend( {}, model, { matchResults: matchResults, currentUser: this.currentUser, translator: translator } );
			_.each( data.matchBets, function( matchBet ) {
				var team1Id = match.team1.teamId;
				var score1 = matchBet.bet.score1;
				var team2Id = match.team2.teamId;
				var score2 = matchBet.bet.score2;
				matchBet[ 'matchResults' ] = service.matchResults( team1Id, score1, team2Id, score2 );
			} );

			this.$el.html( template( data ) );

			return this;
		}
	} );

	return { MatchBetsView: MatchBetsView };
} );
