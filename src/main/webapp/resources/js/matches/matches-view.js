define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateMatchList = require( 'text!js/matches/templates/matches-template.html' );
	var TemplateMatch = require( 'text!js/matches/templates/match-template.html' );
	var TemplateSettings = require( 'text!js/matches/templates/settings-template.html' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Portal page: Matches'
		, betThisMatchButtonTitleLabel: 'Portal page / Matches: Bet this match button title'
		, matchBetLabel: 'Portal page / Matches: user_s match bet label'
	} );

	var MatchesView = Backbone.View.extend( {

		template: _.template( TemplateMatchList ),
		templateSettings: _.template( TemplateSettings ),

		events: {
			'click .matches-settings': '_onSettingsClick'
		},

		initialize: function ( options ) {

			this.categories = Services.loadCategories();
			this.cups = Services.loadCups();
			this.teams = Services.loadTeams();

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.$el.html( this.template( {
				model: this.model
				, translator: translator
			} ) );

			var self = this;
			this.model.forEach( function( match ) {
				self.renderEntry( match );
			});

			return this;
		},

		renderEntry: function ( model ) {

			var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
			} );

			if ( model.isBetMode() ) {
				return this.$( '.match-list-container' ).append( view.renderBetForm().$el );
			}

			return this.$( '.match-list-container' ).append( view.renderMatchInfo().$el );
		},

		_showSettings: function() {
			this.$( '.match-list-container' ).html( this.templateSettings( {
				categories: this.categories
				, cups: this.cups
				, teams: this.teams
				, translator: translator
			} ) );

			return this;
		},

		_onSettingsClick: function( evt ) {
			evt.preventDefault();

			this._showSettings();
		}
	});

	var MatchView = Backbone.View.extend( {

		templateMatch: _.template( TemplateMatch ),

		events: {
			'click .button-bet-match': '_onBetButtonClick'
			, 'click .button-bet-save': '_onSaveBetButtonClick'
			, 'click .button-bet-discard': '_onDiscardButtonClick'
			, 'click .button-edit-bet': '_onBetEditButtonClick'
		},

		initialize: function ( options ) {

			this.categories = options.categories;
			this.cups = options.cups;
			this.teams = options.teams;

			this.model.on( 'sync', this.render, this );
		},

		renderMatchInfo: function () {

			this.$el.html( this.templateMatch( this._getViewOptions() ) );

			var bet = this.model.get( 'bet' );
			if( bet == null ) {
				this.$( '.buttons-cell' ).html( "<button class='fa fa-money button-bet-match'></button>" );

				return this;
			}

			this.$( '.entry-container' ).addClass( 'bg-success' );
			this.$( '.bet-cell' ).html( bet.score1 + ' - ' + bet.score2 );
			this.$( '.buttons-cell' ).html( "<button class='fa fa-edit button-edit-bet'></button>" );
			this.$( '.buttons-cell' ).append( "<button class='fa fa-close button-delete-bet'></button>" );

			return this;
		},

		renderBetForm: function () {

			this.$el.html( this.templateMatch( this._getViewOptions() ) );

			var bet = this.model.get( 'bet' );
			var bet1 = bet != null ? bet.score1 : 0;
			var bet2 = bet != null ? bet.score2 : 0;

			this.$( '.result-1-cell' ).html( "<input class='form-control' id='score1' name='score1' type='number' value='" + bet1 + "'>" );
			this.$( '.result-2-cell' ).html( "<input class='form-control' id='score2' name='score2' type='number' value='" + bet2 + "'>" );

			this.$( '.buttons-cell' ).html( "<button class='fa fa-save button-bet-save'></button>" );
			this.$( '.buttons-cell' ).append( "<button class='fa fa-close button-bet-discard'></button>" );

			return this;
		},

		_getViewOptions: function() {
			var match = this.model.get( 'match' );
			var bet = this.model.get( 'bet' );

			var winnerId = match.score1 > match.score2 ? match.team1Id : match.score1 < match.score2 ? match.team2Id : 0;

			return {
				team1Name: Services.getTeam( this.teams, match.team1Id ).teamName
				, team2Name: Services.getTeam( this.teams, match.team2Id ).teamName
				, style1: winnerId == match.team1Id ? 'text-info' : winnerId == match.team2Id ? 'text-muted' : ''
				, style2: winnerId == match.team2Id ? 'text-info' : winnerId == match.team1Id ? 'text-muted' : ''
				, score1: match.score1
				, score2: match.score2
				, translator: translator
			};
		},

		_saveBet: function() {
			console.log( this.model );

			var match = this.model.get( 'match' );
			var score1 = this.$( '#score1' ).val();
			var score2 = this.$( '#score2' ).val();

			Services.saveBet( match.matchId, score1, score2 );

			this.model.refresh();

			this._goMatchInfoMode();
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