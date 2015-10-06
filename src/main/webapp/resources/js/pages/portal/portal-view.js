define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/portal-template.html' ) );
	var templateDateDependent = _.template( require( 'text!./templates/portal-date-dependant-part-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );
	var activityStreamWidget = require( 'js/widgets/activity-stream/activity-stream-widget' );

	var app = require( 'app' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var menu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noMatchesOnDateLabel: 'No matches on date'
		, todayLabel: 'Today'
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
			, 'click .js-menu-go-to-date' : '_goToDate'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this._renderDateDependant, this );
			this.render();
		},

		render: function () {

			this.$el.html( template() );

			this.model.refresh();

			this._renderActivityStream();
		},

		_renderDateDependant: function () {

			var portalPageDate = this.model.portalPageDate;

			this.$( '.js-date-dependent-part' ).html( templateDateDependent( {
				prevDate: dateTimeService.formatDateFullDisplay( dateTimeService.minusDay( portalPageDate ) )
				, nextDate: dateTimeService.formatDateFullDisplay( dateTimeService.plusDay( portalPageDate ) )
				, translator: translator
			 } ) );

			this._renderDatesMenu( portalPageDate );

			this._renderMatchesOnDate( portalPageDate );

			this._renderMatches( portalPageDate );
		},

		_renderDatesMenu: function( portalPageDate ) {

			var menuItems = [];

			menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-times-o', link: '#', text: translator.todayLabel, entity_id: dateTimeService.formatDate( dateTimeService.dateNow() ) } );
			menuItems.push( { selector: 'divider' } );

			var halfRange = 5;
			var startPeriod = dateTimeService.formatDate( dateTimeService.minusDays( portalPageDate, halfRange ) );
			for( var i = 1; i <= halfRange - 1; i++ ) {
				var date = dateTimeService.plusDays( startPeriod, i );
				var entity_id = dateTimeService.formatDate( date  );
				menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-o', link: '#', text: dateTimeService.formatDateFullDisplay( date ), entity_id: entity_id } );
			}

			menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-check-o', link: '#', selected: true, text: dateTimeService.formatDateFullDisplay( portalPageDate ), entity_id: portalPageDate } );

			for( var j = 1; j <= halfRange; j++ ) {
				var date = dateTimeService.plusDays( portalPageDate, j );
				var entity_id = dateTimeService.formatDate( date  );
				menuItems.push( { selector: 'js-menu-go-to-date', icon: 'fa fa-calendar-o', link: '#', text: dateTimeService.formatDateFullDisplay( date ), entity_id: entity_id } );
			}

			var current = dateTimeService.formatDateFullDisplay( portalPageDate );
			var options = {
				menus: menuItems
				, menuButtonIcon: 'fa-calendar'
				, menuButtonText: current
				, menuButtonHint: current
				, cssClass: 'btn-default'
			};
			menu( options, this.$( '.js-date-current' ) );
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

			this._loadDataForDate();
		},

		_showNextDateMatches: function() {

			this.model.nextDay();

			this._loadDataForDate();
		},

		_goToDate: function( evt ) {

			this.model.portalPageDate = $( evt.target ).data( 'entity_id' );

			this._loadDataForDate();
		},

		_loadDataForDate: function() {

			this.$( '.js-date-current' ).html( '<h4>' + dateTimeService.formatDateFullDisplay( this.model.portalPageDate ) + '</h4>' );

			this.spinning();

			this.model.refresh();
		},

		spinning: function () {

			var div = "<div class='text-center text-muted'><i class='fa fa-spinner fa-spin fa-5x'></i></div>";

			this.$( '.js-portal-page-matches-on-date' ).html( div );
			this.$( '.js-portal-page-container' ).html( div );
		}
	} );
} );

