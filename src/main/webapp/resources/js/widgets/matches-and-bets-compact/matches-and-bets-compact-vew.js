define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-compact-template.html' ) );

	var ConfigurableView = require( 'js/components/widget-configurable/configurable-view' );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams standoff history"
		, yourBetHint: "Your bet"
	} );

	var MatchTransformer = function ( _match, _teamId, _team2Id ) {

		var match = _match;
		var team1Id = _teamId;
		var team2Id = _team2Id;

		return {

			team1: function() {
				if ( team1Id == match.team1.teamId ) {
					return match.team1;
				}

				return match.team2;
			},

			team2: function() {
				if ( team2Id == match.team2.teamId ) {
					return match.team2;
				}

				return match.team1;
			},

			score1: function() {
				if ( team1Id == match.team1.teamId ) {
					return match.score1;
				}

				return match.score2;
			},

			score2: function() {
				if ( team2Id == match.team2.teamId ) {
					return match.score2;
				}

				return match.score1;
			},

			getMatchResults: function() {
				return service.matchResults( this.team1().teamId, this.score1(), this.team2().teamId, this.score2() );
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

		getTitle: function() {
			return this.getTitleHint();
		},

		_renderCupMatchesAndBets: function() {

			var el = this.$( this.windowBodyContainerSelector );
			el.empty();

			var self = this;
			this.model.forEach( function( matchBet ) {
				self._renderEntry( matchBet.toJSON(), el );
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model, el ) {

			var matchTransformer = new MatchTransformer( model.match, this.filter.teamId, this.filter.team2Id );
			var data = _.extend( {}, model, { transformer: matchTransformer, translator: translator } );

			el.append( template( data ) );
		}
	} );
} );