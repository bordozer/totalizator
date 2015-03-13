define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateMatchList = require( 'text!js/matches/templates/matches-template.html' );
	var TemplateMatch = require( 'text!js/matches/templates/match-template.html' );

	var SettingsModel = require( 'js/matches/filter/matches-filter-model' );
	var SettingsView = require( 'js/matches/filter/matches-filter-view' );

	var dateTimeService = require( '/resources/js/dateTimeService.js' );
	var service = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Portal page: Matches'
		, betThisMatchButtonTitleLabel: 'Portal page / Matches: Bet this match button title'
		, matchBetLabel: 'Portal page / Matches: user_s match bet label'
		, allCategoriesLabel: 'Portal page / Matches: All categories label'
		, allCupsLabel: 'Portal page / Matches: All cups label'
	} );

	var MatchesView = Backbone.View.extend( {

		template: _.template( TemplateMatchList ),

		events: {
			'click .matches-settings': '_onSettingsClick'
		},

		initialize: function ( options ) {

			this.categories = service.loadCategories();
			this.cups = service.loadCups();
			this.teams = service.loadTeams();

			this.settingsModel = new SettingsModel( options.settings );
			this.settingsView = new SettingsView( { model: this.settingsModel, el: this.$el } );
			this.settingsView.on( 'events:setting_apply', this._applySettings, this );
			this.settingsView.on( 'events:setting_cancel', this.render, this );

			this.model.on( 'sync', this.render, this );
			this._refresh();
		},

		render: function() {

			var categoryId = this.settingsModel.get( 'categoryId' );
			var cupId = this.settingsModel.get( 'cupId' );

			var filterByCategoryText = categoryId > 0 ? service.getCategory( this.categories, categoryId ).categoryName : translator.allCategoriesLabel;
			var filterByCupText = cupId > 0 ? service.getCup( this.cups, cupId ).cupName : translator.allCupsLabel;
			var title = translator.title + ' / ' + filterByCategoryText + ' / ' + filterByCupText;

			this.$el.html( this.template( {
				model: this.model
				, title: title
				, translator: translator
			} ) );

			this._renderMatches();

			return this;
		},

		_applySettings: function() {
			this._refresh();
		},

		_renderMatches: function() {
			var self = this;
			this.model.forEach( function( match ) {
				self.renderEntry( match );
			});
		},

		renderEntry: function ( model ) {

			var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
			} );

			view.on( 'events:refresh', this._refresh, this );

			return this.$( '.match-list-container' ).append( view.render().$el );
		},

		_refresh: function() {
			var data = this.settingsModel.toJSON();
			this.model.refresh( data );
		},

		_renderSettings: function() {
			this.settingsView.render();
		},

		_onSettingsClick: function( evt ) {
			evt.preventDefault();

			this._renderSettings();
		}
	});

	var MatchView = Backbone.View.extend( {

		templateMatch: _.template( TemplateMatch ),

		events: {
			'click .button-bet-match': '_onBetButtonClick'
			, 'click .button-bet-save': '_onSaveBetButtonClick'
			, 'click .button-bet-discard': '_onDiscardButtonClick'
			, 'click .button-edit-bet': '_onBetEditButtonClick'
			, 'click .button-delete-bet': '_onBetDeleteButtonClick'
		},

		initialize: function ( options ) {

			this.categories = options.categories;
			this.cups = options.cups;
			this.teams = options.teams;

			this.model.on( 'sync', this.render, this );
		},

		render: function() {

			if ( this.model.isBetMode() ) {
				return this.renderBetForm();
			}

			return this.renderMatchInfo();
		},

		renderMatchInfo: function () {

			this.$el.html( this.templateMatch( this._getViewOptions() ) );

			var bet = this.model.get( 'bet' );
			if( bet == null ) {
				this.$( '.buttons-cell' ).html( "<button class='button-icon fa fa-money button-bet-match'></button>" );

				return this;
			}

			this.$( '.entry-container' ).addClass( 'bg-success' );
			this.$( '.bet-cell' ).html( bet.score1 + ' - ' + bet.score2 );
			this.$( '.buttons-cell' ).html( "<button class='button-icon fa fa-edit button-edit-bet'></button>" );
			this.$( '.buttons-cell' ).append( "<button class='button-icon fa fa-close button-delete-bet'></button>" );

			return this;
		},

		renderBetForm: function () {

			this.$el.html( this.templateMatch( this._getViewOptions() ) );

			var bet = this.model.get( 'bet' );
			var bet1 = bet != null ? bet.score1 : 0;
			var bet2 = bet != null ? bet.score2 : 0;

			this.$( '.result-1-cell' ).html( "<input class='form-control' id='score1' name='score1' type='number' value='" + bet1 + "'>" );
			this.$( '.result-2-cell' ).html( "<input class='form-control' id='score2' name='score2' type='number' value='" + bet2 + "'>" );

			this.$( '.buttons-cell' ).html( "<button class='button-icon fa fa-save button-bet-save'></button>" );
			this.$( '.buttons-cell' ).append( "<button class='button-icon fa fa-close button-bet-discard'></button>" );

			return this;
		},

		_getViewOptions: function() {
			var match = this.model.get( 'match' );
			var bet = this.model.get( 'bet' );

			var winnerId = match.score1 > match.score2 ? match.team1Id : match.score1 < match.score2 ? match.team2Id : 0;

			return {
				team1Name: service.getTeam( this.teams, match.team1Id ).teamName
				, team2Name: service.getTeam( this.teams, match.team2Id ).teamName
				, style1: winnerId == match.team1Id ? 'text-info' : winnerId == match.team2Id ? 'text-muted' : ''
				, style2: winnerId == match.team2Id ? 'text-info' : winnerId == match.team1Id ? 'text-muted' : ''
				, score1: match.score1
				, score2: match.score2
				, beginningTime: dateTimeService.formatDateDisplay( match.beginningTime )
				, translator: translator
			};
		},

		_saveBet: function() {

			var match = this.model.get( 'match' );
			var score1 = this.$( '#score1' ).val();
			var score2 = this.$( '#score2' ).val();

			service.saveBet( match.matchId, score1, score2 );

			this.model.setModeMatchInfo();

			this.trigger( 'events:refresh' );
		},

		_deleteBet: function() {

			var match = this.model.get( 'match' );
			var bet = this.model.get( 'bet' );

			service.deleteBet( match.matchId, bet.matchBetId );

			this.model.setModeMatchInfo();

			this.trigger( 'events:refresh' );
		},

		_goBetMode: function() {
			this.model.setModeBet();
			this.renderBetForm();
		},

		_goMatchInfoMode: function() {
			this.model.setModeMatchInfo();
			this.renderMatchInfo();
		},

		_onBetButtonClick: function( evt ) {
			evt.preventDefault();

			this._goBetMode();
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

			this._goMatchInfoMode();
		},

		_onBetEditButtonClick: function( evt ) {
			evt.preventDefault();

			this._goBetMode();
		}
	});

	return { MatchesView: MatchesView };
} );