define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-compact-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var matchBetMenu = require( 'js/widgets/match-bet/match-bet-menu' );
	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var service = require( '/resources/js/services/service.js' );
	var pointsStylist = require( 'js/services/points-stylist' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches"
		, switchViewsLabel: 'Switch match and bets to full views'
		, teamPointsLabel: 'Team points'
		, securedBetHint: 'Bets of another users will be shown after the match start'
		, menuHint: "Match menu"
	} );

	var MatchTransformer = function ( match, bet, team1Id, team2Id ) {

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

	return ConfigurableView.extend( {

		renderInnerView: function ( filter ) {

			this.filter = filter;

			this.currentUser = this.options.currentUser;

			this.listenToOnce( this.model, 'sync', this._render );
			this.model.refresh( filter );
		},

		getIcon: function() {
			return 'fa-futbol-o';
		},

		getPictureURL: function() {
			return service.loadPublicCup( this.filter.cupId ).logoUrl;
		},

		innerViewMenuItems: function() {
			return [ {selector: 'js-switch-views', icon: 'fa fa-newspaper-o', link: '#', text: translator.switchViewsLabel } ];
		},

		_renderCupMatchesAndBets: function() {

			var container = this.$( this.windowBodyContainerSelector );

			var self = this;
			this.model.forEach( function( matchBetModel ) {

				var matchesOnDate = matchBetModel.toJSON();

				var date = matchesOnDate.date;
				var matchBets = matchesOnDate.matchBets;

				var dateEl = $( "<h6 class='well well-sm text-danger'><strong>" + dateTimeService.formatDateFullDisplay( date ) + "</strong></h6>" );
				container.append( dateEl );

				_.each( matchBets, function( matchBet ) {

					var el = $( '<div></div>' );
					container.append( el );

					el.html( self._renderEntry( matchBet ) );
					self._renderMatchMenu( matchBet, el );
				} );
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model ) {

			var matchTransformer = new MatchTransformer( model.match, model.bet, this.filter.teamId, this.filter.team2Id );

			var data = _.extend( {}
					, model
					, {
						transformer: matchTransformer
						, timeToMatchBeginningTime: dateTimeService.fromNow( model.match.beginningTime )
						, translator: translator
					} );

			return template( data );
		},

		_renderMatchMenu: function( matchBet, container ) {

			var menuItems = matchBetMenu.getCommonMenuItems( matchBet );

			var options = {
				menus: menuItems
				, menuButtonIcon: 'fa-list'
				, menuButtonText: ''
				, menuButtonHint: translator.menuHint
				, cssClass: 'btn-default'
			};

			mainMenu( options, $( '.js-match-drop-down-menu', container ) );
		}
	} );
} );