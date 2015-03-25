define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var ConfigurableView = require( 'js/components/configurable-view/configurable-view' );

	var template = _.template( require( 'text!js/matches/templates/matches-template.html' ) );
	var templateMatch = _.template( require( 'text!js/matches/templates/match-template.html' ) );

	var dateTimeService = require( '/resources/js/dateTimeService.js' );
	var service = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Portal page: Matches'
		, betThisMatchButtonTitleLabel: 'Portal page / Matches: Bet this match button title'
		, matchBetLabel: 'Portal page / Matches: user_s match bet label'
		, noMatchBetLabel: 'Portal page / Matches: no match bet yet label'
		, matchFinishedLabel: 'Portal page / Matches: Match finished'
		, createBetButtonHint: 'Portal page / Matches: Create bet button hint'
		, editBetButtonHint: 'Portal page / Matches: Edit bet button hint'
		, deleteBetButtonHint: 'Portal page / Matches: Delete bet button hint'
		, betEditingSaveButtonHint: 'Portal page / Matches: Bet editing save button hint'
		, betEditingCancelButtonHint: 'Portal page / Matches: Bet editing cancel button hint'
	} );

	var MatchesView = ConfigurableView.extend( {

		initialize: function ( options ) {
			this.render();
		},

		renderInnerView: function ( el, filter ) {

			this.model.refresh( filter );

			el.html( template( {
				model: this.model
				, translator: translator
			} ) );

			this._renderMatches();

			return this;
		},

		_renderMatches: function() {
			var self = this;
			this.model.forEach( function( matchBet ) {
				self._renderEntry( matchBet );
			});
		},

		_renderEntry: function ( model ) {

			var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
			} );

			return this.$( '.match-list-container' ).append( view.render().$el );
		}
	});

	var MatchView = Backbone.View.extend( {

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

			this.$el.html( templateMatch( this._getViewOptions() ) );

			var match = this.model.get( 'match' );
			if ( match.matchFinished ) {
				this.$( '.js-panel-footer' ).append( "<div class='row'><div class='col-lg-12'>" + translator.matchFinishedLabel + "</div></div>" );
			}

			var bet = this.model.get( 'bet' );
			if( bet == null ) {

				if ( this.model.isBettingAllowed() ) {
					this._setMatchContainerClass( 'panel-warning' );
					this.$( '.js-panel-footer' ).append( "<div class='col-lg-8'>" + translator.noMatchBetLabel + "</div>" );
					this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-default fa fa-money button-bet-match' title='" + translator.createBetButtonHint + "'></button>" );
				}

				return this;
			}

			this._setMatchContainerClass( 'panel-success' );

			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'>" + translator.matchBetLabel + "</div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 match-bet-score text-right'>" + bet.score1 + "</div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 match-bet-score'>" + bet.score2 + "</div>" );

			if ( ! match.matchFinished ) {
				this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-default fa fa-edit button-edit-bet' title='" + translator.editBetButtonHint + "'></button>" );
				this.$( '.bet-buttons-cell' ).append( "<button class='btn btn-default fa fa-close button-delete-bet' title='" + translator.deleteBetButtonHint + "'></button>" );
			}

			return this;
		},

		renderBetForm: function () {

			this.$el.html( templateMatch( this._getViewOptions() ) );

			var bet = this.model.get( 'bet' );
			var bet1 = bet != null ? bet.score1 : 0;
			var bet2 = bet != null ? bet.score2 : 0;

			this._setMatchContainerClass( 'panel-danger' );

			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'>" + translator.matchBetLabel + "</div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 text-right'><input class='form-control' id='score1' name='score1' type='number' value='" + bet1 + "'></div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'><input class='form-control' id='score2' name='score2' type='number' value='" + bet2 + "'></div>" );

			this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-primary fa fa-save button-bet-save' title='" + translator.betEditingSaveButtonHint + "'></button>" );
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

			var bet = service.saveBet( match.matchId, score1, score2 );

			this.model.set( { bet: bet } );
			this.model.setModeMatchInfo();

			this.render();
		},

		_deleteBet: function() {

			var match = this.model.get( 'match' );
			var bet = this.model.get( 'bet' );

			service.deleteBet( match.matchId, bet.matchBetId );

			this.model.setModeMatchInfo();
			this.model.resetBet();

			this.render();
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