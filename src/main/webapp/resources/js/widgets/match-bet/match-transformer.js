define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var pointsStylist = require( 'js/services/points-stylist' );
	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		securedBetHint: 'Bets of another users will be shown after the match start'
	} );

	return function ( match, bet, team1Id, team2Id ) {

		function _transformMatch( match, team1Id, team2Id ) {

			var result = {};

			result.matchId = match.matchId;
			result.category = match.category;
			result.cup = match.cup;
			result.beginningTime = match.beginningTime;
			result.description = match.description;

			result.matchStarted = match.matchStarted;
			result.matchFinished = match.matchFinished;

			result.showAnotherBets = match.showAnotherBets;

			result.team1 = match.team2;
			result.team2 = match.team1;

			result.score1 = match.score2;
			result.score2 = match.score1;

			result.homeTeamNumber = match.homeTeamNumber == 1 ? 2 : 1;

			return result;
		}

		function _transformBet( bet, matchTransformed ) {

			if ( bet == null ) {
				return null;
			}

			var result = {};

			result.matchBetId = bet.matchBetId;
			result.user = bet.user;
			result.securedBet = bet.securedBet;

			result.match = matchTransformed;

			result.score1 = bet.score2;
			result.score2 = bet.score1;

			return result;
		}

		var _matchTransformed = match;
		var _betTransformed = bet;

		var needTransform = ( team1Id > 0 && match.team1.teamId != team1Id ) || ( team2Id > 0 && match.team2.teamId != team2Id );
		if ( needTransform ) {
			_matchTransformed = _transformMatch( match, team1Id, team2Id );
			_betTransformed = _transformBet( bet,  _matchTransformed );
		}

		return {

			team1: function() {
				return _matchTransformed.team1;
			},

			team2: function() {
				return _matchTransformed.team2;
			},

			score1: function() {
				return _matchTransformed.score1;
			},

			score2: function() {
				return _matchTransformed.score2;
			},

			homeTeamNumber: function() {
				return _matchTransformed.homeTeamNumber;
			},

			betScore1: function() {

				if ( _betTransformed == null ) {
					return 0;
				}

				if ( _betTransformed.securedBet ) {
					return this._securedBet();
				}

				return _betTransformed.score1;
			},

			betScore2: function() {

				if ( _betTransformed == null ) {
					return 0;
				}

				if ( _betTransformed.securedBet ) {
					return this._securedBet();
				}

				return _betTransformed.score2;
			},

			_securedBet: function() {
				return "<span class='fa fa-lock' title='" + translator.securedBetHint + ' ( ' + dateTimeService.fromNow( match.beginningTime ) + " )'></span>";
			},

			getMatchResults: function() {
				return service.matchResults( this.team1().teamId, this.score1(), this.team2().teamId, this.score2() );
			},

			getBetScoreHighlights: function() {
				return pointsStylist.styleBetPoints( _matchTransformed, _betTransformed );
			},

			formatTime: function() {
				return dateTimeService.formatTimeDisplay( match.beginningTime );
			}
		}
	};
} );