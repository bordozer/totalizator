define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateMatchList = require( 'text!js/matches/templates/matches-template.html' );
	var TemplateMatch = require( 'text!js/matches/templates/match-template.html' );
	var TemplateBetAMatch = require( 'text!js/matches/templates/bet-a-match-template.html' );

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

			/*if ( model.get( 'matchId' ) == 0 ) {
				return this.$( '.match-list-container' ).append( view.renderBetForm().$el );
			}*/

			return this.$( '.match-list-container' ).append( view.renderMatchInfo().$el );
		}
	});

	var MatchView = Backbone.View.extend( {

		templateMatch: _.template( TemplateMatch ),
		templateBetForm: _.template( TemplateBetAMatch ),

		initialize: function ( options ) {

			this.categories = options.categories;
			this.cups = options.cups;
			this.teams = options.teams;

			this.model.on( 'sync', this.render, this );
		},

		renderMatchInfo: function () {

			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateMatch( {
				model: modelJSON
				, team1Id: modelJSON.team1Id
				, team1Name: Services.getTeam( this.teams, modelJSON.team1Id ).teamName
				, score1: modelJSON.score1
				, team2Id: modelJSON.team2Id
				, team2Name: Services.getTeam( this.teams, modelJSON.team2Id ).teamName
				, score2: modelJSON.score2
				, winnerId: modelJSON.score1 > modelJSON.score2 ? modelJSON.team1Id : modelJSON.score1 < modelJSON.score2 ? modelJSON.team2Id : 0
				, translator: translator
			} ) );

			return this;
		},

		renderBetForm: function () {

			var modelJSON = this.model.toJSON();

			this.$el.html( this.templateBetForm( {
				model: modelJSON
				, translator: translator
			} ) );

			return this;
		}
	});

	return { MatchesView: MatchesView };
} );