define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var moment = require( 'moment' );

	var widgetNoContainerTemplate = _.template( require( 'text!./templates/widget-no-container-template.html' ) );

	var templateTable = _.template( require( 'text!./templates/match-and-bet-table-entry-template.html' ) );
	var templateMinimized = _.template( require( 'text!./templates/match-and-bet-minimized-template.html' ) );

	var MatchTeamsView = require( './teams/match-teams-view' );
	var BetZoneView = require( './bet-zone/bet-zone-view' );
	var MatchDescriptionView = require( './description/match-description-view' );
	var StandoffView = require( './standoff/standoff-view' );

	var MatchTransformer = require( './match-transformer' );

	var matchBetMenu = require( './match-bet-menu' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var app = require( 'app' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		actionMatchBetAdd: 'Match and Bets: Add bet'
		, actionMatchBetEdit: 'Match and Bets: Edit bet'
		, actionMatchBetSave: 'Match and Bets: save bet'
		, actionCancelBetEditing: 'Match and Bets: Cancel bet editing'
		, actionMatchBetDelete: 'Match and Bets: Delete bet'

		, deleteBetConfirmationLabel: 'Match and Bets: Delete bet confirmation: Delete bet?'

		, matchBettingIsDenied: "Match betting is denied"
		, pointsLabel: "Points"
	} );

	var MODE_INFO = 1;
	var MODE_EDIT = 2;
	var MODE_DESCRIPTION = 3;

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	return WidgetView.extend( {

		events: {
			'click .button-bet-match, .js-menu-match-bet-add': '_onBetButtonClick'
			, 'click .button-bet-save, .js-menu-match-bet-save': '_onSaveBetButtonClick'
			, 'click .button-bet-discard, .js-menu-match-bet-cancel-editing': '_onDiscardButtonClick'
			, 'click .button-edit-bet, .js-menu-match-bet-edit': '_onBetEditButtonClick'
			, 'click .button-delete-bet, .js-menu-match-bet-delete': '_onBetDeleteButtonClick'
			, 'click .js-menu-match-description': '_onMatchDescriptionClick'
			, 'click .js-close-match-description': '_onCloseMatchDescriptionClick'
		},

		initialize: function ( options ) {

			this.currentUser = app.currentUser();
			this.matchViewMode = options.options.matchViewMode;
			this.filter = options.options.filter;

			this._setWidgetModes( MODE_INFO );

			this.menuItems = [];

			this.model.on( 'sync', this._render, this );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			var model = this.model.toJSON();
			var match = model.match;

			var beginningDate = dateTimeService.formatDateDisplay( match.beginningTime );
			var beginningTime = dateTimeService.formatTimeDisplay( match.beginningTime );
			var timeToStart = dateTimeService.fromNow( match.beginningTime );

			return "<strong title='#" + model.matchId + "'>" + beginningDate + " <sup>" + beginningTime + "</sup></strong> - <small>" + timeToStart + "</small>"
		},

		getIcon: function () {
			var match = this.model.toJSON().match;

			if ( ! match ) {
				return 'fa-hourglass-half';
			}

			if ( match.matchFinished ) {
				return 'fa-flag-checkered';
			}

			if ( match.matchStarted ) {
				return 'fa-futbol-o';
			}

			return 'fa-clock-o';
		},

		getPictureURL: function() {
			return this.model.toJSON().match.cup.logoUrl;
		},

		getCustomMenuItems: function() {
			return this.menuItems;
		},

		widgetBodyMinHeight: function() {
			return '110px';
		},

		_render: function() {

			if ( this.mode == MODE_EDIT ) {

				this._renderBetForm();

				this.trigger( 'inner-view-rendered' );

				return;
			}

			if ( this.mode == MODE_DESCRIPTION ) {

				this._renderMatchDescription();

				this.trigger( 'inner-view-rendered' );

				return;
			}

			this._renderMatchInfo( this.matchViewMode );

			this.trigger( 'inner-view-rendered' );
		},

		_renderMatchInfo: function ( matchViewMode ) {

			var matchBet = this.model.toJSON();

			if ( matchViewMode == VIEW_MODE_TABLE ) {
				this.$( this.windowBodyContainerSelector ).html( this._renderTableEntry( this.model.toJSON() ) );
				this._renderDropDownMenuItems( matchBetMenu.getMenuItems( matchBet, MODE_INFO, this.matchViewMode ) );
				return;
			}

			if ( matchViewMode == VIEW_MODE_MINIMIZED ) {
				this.$( this.windowBodyContainerSelector ).html( this._renderMinimizedEntry( this.model.toJSON() ) );
				this._renderDropDownMenuItems( matchBetMenu.getMenuItems( matchBet, MODE_INFO, this.matchViewMode ) );
				return;
			}

			this._renderMatchTeams();

			var view = new BetZoneView( {
				matchBet: matchBet
				, isEdit: false
				, showBetForUserId: this.model.showBetForUserId
				, currentUser: this.currentUser
				, el: this.$( '.js-footer' )
			} );

			this._renderDropDownMenuItems( matchBetMenu.getMenuItems( matchBet, MODE_INFO, this.matchViewMode ) );

			this._setMatchContainerClass( this._getPanelClass() );
		},

		_renderBetForm: function () {

			var matchBet = this.model.toJSON();

			new StandoffView( {
				cup: matchBet.match.cup
				, team1: matchBet.match.team1
				, team2: matchBet.match.team2
				, el: this.$( this.windowBodyContainerSelector )
			} );

			var view = new BetZoneView( {
				matchBet: matchBet
				, isEdit: true
				, showBetForUserId: this.model.showBetForUserId
				, currentUser: this.currentUser
				, el: this.$( '.js-footer' )
			} );
			view.on( 'events:save_bet', this._saveBet, this );

			this._renderDropDownMenuItems( matchBetMenu.getMenuItems( matchBet, MODE_EDIT, this.matchViewMode ) );

			this._setMatchContainerClass( 'panel-danger' );
		},

		_renderMatchDescription: function () {

			var matchBet = this.model.toJSON();

			new MatchDescriptionView( {
				match: matchBet.match
				, el: this.$( this.windowBodyContainerSelector )
				, footerEl: this.$( '.js-footer' )
			} );

			this._renderDropDownMenuItems( matchBetMenu.getMenuItems( matchBet, MODE_DESCRIPTION, this.matchViewMode ) );
		},

		_renderTableEntry: function ( model ) {

			var matchTransformer = new MatchTransformer( model.match, model.bet, this.filter.teamId, this.filter.team2Id );

			var data = _.extend( {}
					, model
					, {
						transformer: matchTransformer
						, timeToMatchBeginningTime: dateTimeService.fromNow( model.match.beginningTime )
						, translator: translator
					} );

			return templateTable( data );
		},

		_renderMinimizedEntry: function ( model ) {

			function __getBoxColor() {

				if ( model.bet == null ) {
					return model.match.matchStarted ? 'bg-missed-bet' : '';
				}

				if ( ! model.match.matchFinished ) {
					return 'bg-warning';
				}

				if ( ! model.userMatchPointsHolder ) {
					return 'bg-info';
				}

				if ( model.userMatchPointsHolder.summaryPoints > 0 ) {
					return 'bg-success';
				}

				if ( model.userMatchPointsHolder.summaryPoints < 0 ) {
					return 'bg-danger';
				}

				return '';
			}

			var matchTransformer = new MatchTransformer( model.match, model.bet, this.filter.teamId, this.filter.team2Id );
			var data = _.extend( {}, model, {
				beginningDate: dateTimeService.formatDateDisplay( model.match.beginningTime )
				, beginningTime: dateTimeService.formatTimeDisplay( model.match.beginningTime )
				, transformer: matchTransformer
				, timeToMatchBeginningTime: dateTimeService.fromNow( model.match.beginningTime )
				, colorCss: __getBoxColor()
				, translator: translator}
			);

			return templateMinimized( data );
		},

		_renderMatchTeams: function() {

			var model = this.model.toJSON();
			var match = model.match;

			new MatchTeamsView( {
				match: match
				, el: this.$( this.windowBodyContainerSelector )
			} );
		},

		_getPanelClass: function() {

			var matchBet = this.model.toJSON();
			var match = matchBet.match;
			var isMatchFinished = match.matchFinished;
			var bet = matchBet.bet;
			var isBet = bet != null;

			var userMadeBetButMatchNotFinishedYet = isBet && ! isMatchFinished;
			if ( userMadeBetButMatchNotFinishedYet ) {
				return 'panel-warning';
			}

			var matchFinishedAndUserGotSomePoints = isMatchFinished && matchBet.userMatchPointsHolder.matchBetPoints > 0;
			if ( matchFinishedAndUserGotSomePoints ) {
				return 'panel-success';
			}

			return 'panel-default';
		},

		_renderDropDownMenuItems: function( menuItems ) {
			this.menuItems = menuItems;
			this._renderDropDownMenu();
		},

		_setMatchContainerClass: function( clazz ) {
			this.setPanelClass( clazz );
		},

		_saveBet: function() {

			var match = this.model.get( 'match' );
			var score1 = this.$( '#score1' ).val();
			var score2 = this.$( '#score2' ).val();

			var bet = service.saveBet( match.matchId, score1, score2 );

			this.model.set( { bet: bet } );

			this._setWidgetModes( MODE_INFO );

			this.render();

			this.trigger( 'events:match_bet_is_changed', bet );
		},

		_deleteBet: function() {

			if ( ! this._assertBetting() ) {
				return;
			}

			if ( ! confirm( translator.deleteBetConfirmationLabel ) ) {
				return;
			}

			var match = this.model.get( 'match' );
			var bet = this.model.get( 'bet' );

			service.deleteBet( match.matchId, bet.matchBetId );

			this.model.resetBet();

			this._setWidgetModes( MODE_INFO );

			this.render();

			this.trigger( 'events:match_bet_is_changed', null );
		},

		_onBetButtonClick: function( evt ) {
			evt.preventDefault();

			if ( ! this._assertBetting() ) {
				return;
			}

			this._setWidgetModes( MODE_EDIT );

			this.render();
		},

		_onSaveBetButtonClick: function( evt ) {
			evt.preventDefault();

			this._saveBet();
		},

		_onBetDeleteButtonClick: function( evt ) {
			evt.preventDefault();

			this._deleteBet();
		},

		_onDiscardButtonClick: function( evt ) {
			evt.preventDefault();

			this._setWidgetModes( MODE_INFO );

			this.render();
		},

		_onBetEditButtonClick: function( evt ) {
			evt.preventDefault();

			if ( ! this._assertBetting() ) {
				return;
			}

			this._setWidgetModes( MODE_EDIT );

			this.render();
		},

		_onMatchDescriptionClick: function() {

			this._setWidgetModes( MODE_DESCRIPTION );

			this.render();
		},

		_onCloseMatchDescriptionClick: function() {

			this._setWidgetModes( MODE_INFO );

			this.render();
		},

		_assertBetting: function() {

			if ( ! this.model.isBettingAllowed() ) {
				alert( translator.matchBettingIsDenied );
				return false;
			}

			return true;
		},

		_setWidgetModes: function( mode ) {

			this.mode = mode;

			if ( this.mode == MODE_INFO && ( this.matchViewMode == VIEW_MODE_TABLE || this.matchViewMode == VIEW_MODE_MINIMIZED ) ) {
				this.widgetTemplate = widgetNoContainerTemplate;
				return;
			}

			this.widgetTemplate = this.getDefaultWidgetTemplate();
		}
	});
} );
