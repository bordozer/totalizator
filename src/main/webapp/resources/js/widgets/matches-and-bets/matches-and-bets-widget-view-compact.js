define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-compact-widget-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var MatchMenu = require( './match/match-menu-view' );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches"
		, noMatchesFound: "No matches found"
		, switchViewsLabel: 'Switch match and bets to full views'
		, teamPointsLabel: 'Team points'
	} );

	var MatchTransformer = function ( _match, _bet, _teamId, _team2Id, _points ) {

		var match = _match;
		var bet = _bet;
		var team1Id = _teamId;
		var team2Id = _team2Id;
		var points = _points;

		return {

			team1: function() {
				if ( team1Id == 0 || team2Id == 0 ) {
					return match.team1;
				}

				if ( team1Id == match.team1.teamId ) {
					return match.team1;
				}

				return match.team2;
			},

			team2: function() {
				if ( team1Id == 0 || team2Id == 0 ) {
					return match.team2;
				}

				if ( team2Id == match.team2.teamId ) {
					return match.team2;
				}

				return match.team1;
			},

			score1: function() {
				if ( team1Id == 0 || team2Id == 0 ) {
					return match.score1;
				}

				if ( team1Id == match.team1.teamId ) {
					return match.score1;
				}

				return match.score2;
			},

			score2: function() {
				if ( team1Id == 0 || team2Id == 0 ) {
					return match.score2;
				}

				if ( team2Id == match.team2.teamId ) {
					return match.score2;
				}

				return match.score1;
			},

			homeTeamNumber: function() {
				if ( team1Id == 0 || team2Id == 0 ) {
					return match.homeTeamNumber;
				}

				if ( team1Id == match.team1.teamId ) {
					return match.homeTeamNumber;
				}

				return match.homeTeamNumber == 1 ? 2 : 1;
			},

			betScore1: function() {
				if ( bet == null ) {
					return 0;
				}

				if ( team1Id == 0 || team2Id == 0 ) {
					return bet.score1;
				}

				if ( team1Id == match.team1.teamId ) {
					return bet.score1;
				}

				return bet.score2;
			},

			betScore2: function() {
				if ( bet == null ) {
					return 0;
				}

				if ( team1Id == 0 || team2Id == 0 ) {
					return bet.score2;
				}

				if ( team2Id == match.team2.teamId ) {
					return bet.score2;
				}

				return bet.score1;
			},

			getMatchResults: function() {
				return service.matchResults( this.team1().teamId, this.score1(), this.team2().teamId, this.score2() );
			},

			getBetScoreHighlights: function() {

				if ( ! match.matchFinished ) {
					return { style1: 'text-muted', style2: 'text-muted' };
				}

				if( bet != null && points == 0 ) {
					return { style1: 'text-danger', style2: 'text-danger' };
				}

				return service.getBetScoreHighlights( this.team1().teamId, this.betScore1(), this.team2().teamId, this.betScore2() );
			},

			formatDate: function() {
				return dateTimeService.formatDateDisplay( match.beginningTime );
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

			this.listenToOnce( this.model, 'sync', this._renderCupMatchesAndBets );
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
			container.empty();

			if ( this.model.length == 0 ) {
				container.html( translator.noMatchesFound );
				this.trigger( 'inner-view-rendered' );

				return;
			}

			var self = this;
			this.model.forEach( function( matchBet ) {

				var el = $( '<div></div>' );
				container.append( el );

				el.html( self._renderEntry( matchBet.toJSON() ) );
				self._renderMatchMenu( matchBet, el );
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model ) {

			var matchTransformer = new MatchTransformer( model.match, model.bet, this.filter.teamId, this.filter.team2Id, model.points );
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
			var matchMenu = new MatchMenu( {
				model: matchBet
				, el: $( '.js-match-drop-down-menu', container )
			} );
		}
	} );
} );