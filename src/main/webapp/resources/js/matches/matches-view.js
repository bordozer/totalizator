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
		, noMatchBetLabel: 'Portal page / Matches: no match bet yet label'
		, matchFinishedLabel: 'Portal page / Matches: Match finished'
		, allCategoriesLabel: 'Portal page / Matches: All categories label'
		, allCupsLabel: 'Portal page / Matches: All cups label'
		, createBetButtonHint: 'Portal page / Matches: Create bet button hint'
		, editBetButtonHint: 'Portal page / Matches: Edit bet button hint'
		, deleteBetButtonHint: 'Portal page / Matches: Delete bet button hint'
		, betEditingSaveButtonHint: 'Portal page / Matches: Bet editing save button hint'
		, betEditingCancelButtonHint: 'Portal page / Matches: Bet editing cancel button hint'
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
			this.model.forEach( function( matchBet ) {
				self.renderEntry( matchBet );
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

//			console.log( this.model );

			this.$el.html( this.templateMatch( this._getViewOptions() ) );

			var match = this.model.get( 'match' );
			if ( match.matchFinished ) {
				this.$( '.js-match-container' ).addClass( 'bg-info' );
			}

			var bet = this.model.get( 'bet' );
			if( bet == null ) {

				if ( this.model.isBettingAllowed() ) {
					this._setMatchContainerClass( 'panel-warning' );
					this.$( '.js-panel-footer' ).append( "<div class='col-lg-8'>" + translator.noMatchBetLabel + "</div>" );
					this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-default fa fa-money button-bet-match' title='" + translator.createBetButtonHint + "'></button>" );
				}

				if ( match.matchFinished ) {
					this.$( '.js-panel-footer' ).append( "<div class='row'><div class='col-lg-12'>" + translator.matchFinishedLabel + "</div></div>" );
				}

				return this;
			}

			this._setMatchContainerClass( 'panel-success' );

			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'>" + translator.matchBetLabel + "</div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 text-right'>" + bet.score1 + "</div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'>" + bet.score2 + "</div>" );

			if ( ! match.matchFinished ) {
				this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-default fa fa-edit button-edit-bet' title='" + translator.editBetButtonHint + "'></button>" );
				this.$( '.bet-buttons-cell' ).append( "<button class='btn btn-default fa fa-close button-delete-bet' title='" + translator.deleteBetButtonHint + "'></button>" );
			}

			return this;
		},

		renderBetForm: function () {

			this.$el.html( this.templateMatch( this._getViewOptions() ) );

			var bet = this.model.get( 'bet' );
			var bet1 = bet != null ? bet.score1 : 0;
			var bet2 = bet != null ? bet.score2 : 0;

			this._setMatchContainerClass( 'panel-danger' );

			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'>" + translator.matchBetLabel + "</div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 text-right'><input class='form-control' id='score1' name='score1' type='number' value='" + bet1 + "'></div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'><input class='form-control' id='score2' name='score2' type='number' value='" + bet2 + "'></div>" );

			this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-default fa fa-save button-bet-save' title='" + translator.betEditingSaveButtonHint + "'></button>" );
			this.$( '.bet-buttons-cell' ).append( "<button class='btn btn-default fa fa-close button-bet-discard' title='" + translator.betEditingCancelButtonHint + "'></button>" );

			return this;
		},

		_setMatchContainerClass: function( clazz ) {
			this.$( '.js-match-container' ).removeClass( 'panel-default' );
			this.$( '.js-match-container' ).addClass( clazz );
		},

		_getViewOptions: function() {
			var match = this.model.get( 'match' );
			var bet = this.model.get( 'bet' );

			var winnerId = match.score1 > match.score2 ? match.team1Id : match.score1 < match.score2 ? match.team2Id : 0;

			return {
				matchId: match.matchId
				, team1Name: service.getTeam( this.teams, match.team1Id ).teamName
				, team2Name: service.getTeam( this.teams, match.team2Id ).teamName
				, style1: winnerId == match.team1Id ? 'text-info' : winnerId == match.team2Id ? 'text-muted' : ''
				, style2: winnerId == match.team2Id ? 'text-info' : winnerId == match.team1Id ? 'text-muted' : ''
				, score1: match.score1
				, score2: match.score2
				, team1Logo: match.team1Logo
				, team2Logo: match.team2Logo
				, beginningTime: dateTimeService.formatDateDisplay( match.beginningTime )
				, matchFinished: match.matchFinished
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