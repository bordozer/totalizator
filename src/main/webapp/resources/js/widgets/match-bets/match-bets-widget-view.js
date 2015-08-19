define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bets-templates.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var app = require( 'app' );
	var service = require( '/resources/js/services/service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Match bests"
		, userLabel: "User"
		, userPointsLabel: "Points"
		, matchBestsLabel: "Match bests"
		, anotherBetsAreHidden: "Bets of another users will be shown after the match start"
	} );

	return WidgetView.extend( {

		events: {
		},

		initialize: function( options ) {

			this.userName = options.options.userName;

			this.listenTo( this.model, 'sync', this._renderMatchBets );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-futbol-o';
		},

		_renderMatchBets: function() {

			var model = this.model.toJSON();
			var match = model.match;

			var data = _.extend( {}
				, model
				, {
					matchResults: service.matchResultsByMatch( match )
					, timeToBetPublishing: dateTimeService.fromNow( match.beginningTime )
					, currentUser: app.currentUser()
					, matchBeginningTime: dateTimeService.formatDateTimeDisplay( match.beginningTime )
					, translator: translator
			} );

			_.each( data.matchBets, function( matchBet ) {

				var team1Id = match.team1.teamId;
				var score1 = matchBet.bet.score1;
				var team2Id = match.team2.teamId;
				var score2 = matchBet.bet.score2;
				matchBet[ 'matchResults' ] = service.matchResults( team1Id, score1, team2Id, score2 );
			} );

			this.$( this.windowBodyContainerSelector ).html( template( data ) );

			this.trigger( 'inner-view-rendered' );
		}
	});
} );


