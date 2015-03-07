define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateMatchList = require( 'text!js/matches/templates/matches-template.html' );
	var TemplateMatch = require( 'text!js/matches/templates/match-template.html' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Portal page: Matches'
		, betThisMatchButtonTitleLabel: 'Portal page / Matches: Bet this match button title'
	} );

	var MatchesView = Backbone.View.extend( {

		template: _.template( TemplateMatchList ),

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
		}
	});

	var MatchView = Backbone.View.extend( {

		templateMatch: _.template( TemplateMatch ),

		events: {
			'click .button-bet-match': '_onBetButtonClick'
			, 'click .button-bet-discard': '_onDiscardButtonClick'
		},

		initialize: function ( options ) {

			this.categories = options.categories;
			this.cups = options.cups;
			this.teams = options.teams;

			this.model.on( 'sync', this.render, this );
		},

		renderMatchInfo: function () {

			this.$el.html( this.templateMatch( this._getViewOptions( this.model ) ) );

			this.$( '.result-1-cell' ).html( this.model.get( 'score1' ) );
			this.$( '.result-2-cell' ).html( this.model.get( 'score1' ) );

			this.$( '.buttons-cell' ).html( "<button class='fa fa-money button-bet-match'></button>" );

			return this;
		},

		renderBetForm: function () {

			this.$el.html( this.templateMatch( this._getViewOptions( this.model ) ) );

			this.$( '.result-1-cell' ).html( "<input class='form-control' type='text' value='' size='2'>" );
			this.$( '.result-2-cell' ).html( "<input class='form-control' type='text' value='' size='2'>" );

			this.$( '.buttons-cell' ).html( "<button class='fa fa-save button-bet-save'></button>" );
			this.$( '.buttons-cell' ).append( "<button class='fa fa-close button-bet-discard'></button>" );

			return this;
		},

		_getViewOptions: function( model ) {
			var modelJSON = this.model.toJSON();
			var winnerId = modelJSON.score1 > modelJSON.score2 ? modelJSON.team1Id : modelJSON.score1 < modelJSON.score2 ? modelJSON.team2Id : 0;

			return {
				model: modelJSON
				, team1Id: modelJSON.team1Id
				, team1Name: Services.getTeam( this.teams, modelJSON.team1Id ).teamName
				, score1: modelJSON.score1
				, team2Id: modelJSON.team2Id
				, team2Name: Services.getTeam( this.teams, modelJSON.team2Id ).teamName
				, score2: modelJSON.score2
				, style1: winnerId == modelJSON.team1Id ? 'text-info' : winnerId == modelJSON.team2Id ? 'text-muted' : ''
				, style2: winnerId == modelJSON.team2Id ? 'text-info' : winnerId == modelJSON.team1Id ? 'text-muted' : ''
				, translator: translator
			};
		},

		_betMatch: function() {
			this.model.setModeBet();

			this.renderBetForm();
		},

		_onBetButtonClick: function( evt ) {
			evt.preventDefault();

			this._betMatch();
		},

		_discardBet: function() {
			this.model.setModeMatchInfo();

			this.renderMatchInfo();
		},

		_onDiscardButtonClick: function( evt ) {
			evt.preventDefault();

			this._discardBet();
		}
	});

	return { MatchesView: MatchesView };
} );