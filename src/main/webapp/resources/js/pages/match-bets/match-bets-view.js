define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bets-template.html' ) );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches and bests"
		, userLabel: "User"
		, userPointsLabel: "Points"
		, matchBestsLabel: "Match bests"
		, anotherBetsAreHidden: "Bets of another users will be shown after the match start"
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

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } );

			var model = this.model.toJSON();

			var match = model.match;

			var matchBeginningTime = dateTimeService.formatDateTimeDisplay( match.beginningTime );

			var matchResults = service.matchResultsByMatch( match );

			var data = _.extend( {}
					, model
					, {
						matchResults: matchResults
						, timeToOpenBet: dateTimeService.fromNow( match.beginningTime )
						, currentUser: this.currentUser
						, matchBeginningTime: matchBeginningTime
						, translator: translator
					} );

			_.each( data.matchBetsSecured, function( matchBetSecured ) {

				var matchBet = matchBetSecured.matchBet;

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
