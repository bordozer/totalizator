define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/portal-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );
	var activityStreamWidget = require( 'js/widgets/activity-stream/activity-stream-widget' );

	var app = require( 'app' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noMatchesOnDateLabel: 'No matches on date'
	} );

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;

	return Backbone.View.extend( {

		events: {
			'click .js-date-before' : '_showPreviousDateMatches'
			, 'click .js-date-after' : '_showNextDateMatches'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.refresh();
		},

		render: function () {

			var portalPageDate = this.model.portalPageDate;

			this.$el.html( template( {
				prevDate: dateTimeService.formatDateFullDisplay( dateTimeService.minusDay( portalPageDate ) )
				, currentDate: dateTimeService.formatDateFullDisplay( portalPageDate )
				, nextDate: dateTimeService.formatDateFullDisplay( dateTimeService.plusDay( portalPageDate ) )
				, translator: translator
			 } ) );

			this._renderMatchesOnDate( portalPageDate );

			this._renderMatches( portalPageDate );

			this._renderActivityStream();
		},

		_renderMatchesOnDate: function ( onDate ) {

			var model = this.model.toJSON();
			var currentUser = app.currentUser();

			var container = this.$( '.js-portal-page-matches-on-date' );

			if ( model.cupsTodayToShow.length == 0 ) {
				container.html( translator.noMatchesOnDateLabel );
				return;
			}

			_.each( model.cupsTodayToShow, function( cup ) {

				var el = $( '<div></div>' );
				container.append( el );

				var options = {
					filter: {
						categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFutureMatches: true
						, showFinished: true
						, filterByDateEnable: true
						, filterByDate: onDate
					}
					, matchViewMode: VIEW_MODE_MINIMIZED
					, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_MATCHES
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			} );
		},

		_renderMatches: function( onDate ) {

			var model = this.model.toJSON();

			var currentUser = app.currentUser();

			var container = this.$( '.js-portal-page-container' );

			var row = $( "<div class='row'></div>" );
			container.append( row );

			_.each( model.cupsToShow, function( cup ) {

				var el = $( '<div class="col-xs-12"></div>' );
				row.append( el );

				var options = {
					filter: {
						categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFutureMatches: true
						, filterByDate: onDate
						, filterByDateEnable: true
					}
					, matchViewMode: VIEW_MODE_BET
					, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_STATISTICS
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			} );
		},

		_renderActivityStream: function () {
			activityStreamWidget( this.$( '.js-portal-page-activity-stream' ), {} );
		},

		_showPreviousDateMatches: function() {

			this.model.previousDay();

			this.spinning();

			this.model.refresh();
		},

		_showNextDateMatches: function() {

			this.model.nextDay();

			this.spinning();

			this.model.refresh();
		},

		spinning: function () {
			this.$( '.js-portal-page-matches-on-date' ).html( "<div class='text-center'><i class='fa fa-spinner fa-spin fa-5x'></i></div>" );
			this.$( '.js-portal-page-container' ).html( "<div class='text-center'><i class='fa fa-spinner fa-spin fa-5x'></i></div>" );
		}
	} );
} );

