define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-and-bets-widget-collapsed-template.html' ) );

	var CollapsedStateModel = require( './matches-and-bets-widget-collapsed-model' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		matchesCount: "Matches count total"
		, futureMatchesCount: "Future matches count"
		, todayMatchesCountLabel: "Matches on date"
		, userBetsCount: "User bets count"
		, matchesWithoutBetsCount: "Matches without bets count"
		, firstMatchTimeLabel: "First match beginning time"
		, firstMatchNoBetTimeTitleLabel: "First match without bet beginning time title"
		, cupWinnerBetIsAccessibleLabel: "Cup winner bet is accessible"
		, userHasMissedCupWinnerBettingLabel: "User has missed cup winner betting"
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {

			this.filter = options.filter;

			this.model = new CollapsedStateModel();
			this.listenToOnce( this.model, 'sync', this.render );

			this.model.refresh( this.filter );
		},

		render: function () {

			var model = this.model.toJSON();

			var data = _.extend( {}, model, {
				matchesWithoutBetsCountStyle: this._getMatchesWithoutBetsCountStyle()
				, firstMatchTime: model.firstMatchTime == null ? '' : dateTimeService.formatDateTimeFullDisplay( model.firstMatchTime )
				, firstMatchTimeHumanize: model.firstMatchTime == null ? '' : dateTimeService.fromNow( model.firstMatchTime )
				, firstMatchNoBetTime: model.firstMatchNoBetTime == null ? '' : dateTimeService.formatDateTimeFullDisplay( model.firstMatchNoBetTime )
				, firstMatchNoBetTimeHumanize: model.firstMatchNoBetTime == null ? '' : dateTimeService.fromNow( model.firstMatchNoBetTime )
				, theDate: dateTimeService.formatDateDisplay( this.filter.filterByDate )
				, translator: translator
			} );

			this.$el.html( template( data ) );
		},

		_getMatchesWithoutBetsCountStyle: function() {

			var model = this.model.toJSON();

			if ( model.matchesWithoutBetsCount > 0 ) {
				return 'text-danger';
			}

			return 'text-muted';
		}
	} );
} );