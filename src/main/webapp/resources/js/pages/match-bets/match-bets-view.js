define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bets-template.html' ) );

	var MatchBetsWidget = require( 'js/widgets/match-bets/match-bets-widget' );
	var MatchBetWidget = require( 'js/widgets/match-bet/match-bet-widget' );

	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches and bests"
		, anotherBetsAreHidden: "Bets of another users will be shown after the match start"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cupId = options.options.cupId;
			this.matchId = options.options.matchId;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			var match = this.model.toJSON();

			this.trigger( 'navigation:set:active:cup', { selectedCup: match.cup } );

			var data = _.extend( {}, {
				match: match
				, matchResults: service.matchResultsByMatch( match )
				, matchBeginningTime: dateTimeService.formatDateTimeDisplay( match.beginningTime )
				, timeToBetPublishing: dateTimeService.fromNow( match.beginningTime )
				, translator: translator
			} );
			this.$el.html( template( data ) );

			this._renderMatchBetsWidget();

			this._renderMatchBetWidget();

			return this;
		},

		_renderMatchBetsWidget: function() {

			var model = this.model.toJSON();
			var match = model.match;

			var options = {
				matchId: model.matchId
				, showBetForUserId: this.currentUser.userId
			};

			var view = new MatchBetsWidget( this.$( '.js-match-bets-widget' ), options );
		},

		_renderMatchBetWidget: function() {

			var model = this.model.toJSON();
			var match = model.match;

			var options = {
				matchId: model.matchId
				, showBetForUserId: this.currentUser.userId
				, matchViewMode: 1
			};

			var view = new MatchBetWidget( this.$( '.js-match-bet-widget' ), options ).view();
			view.on( 'events:match_bet_is_changed', this._onUserChangeBet, this );
		},

		_onUserChangeBet: function( bet ) {
			this._renderMatchBetsWidget();
		}
	} );
} );
