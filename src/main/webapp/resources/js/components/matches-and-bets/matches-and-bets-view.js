define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var ConfigurableView = require( 'js/components/configurable-view/configurable-view' );

	var templateMatch = _.template( require( 'text!./templates/matches-and-bets-entry-template.html' ) );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var service = require( '/resources/js/services/service.js' );
	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		actionMatchBetAdd: 'Match and Bets: Add bet'
		, actionMatchBetEdit: 'Match and Bets: Edit bet'
		, actionMatchBetSave: 'Match and Bets: save bet'
		, actionCancelBetEditing: 'Match and Bets: Cancel bet editing'
		, actionMatchBetDelete: 'Match and Bets: Delete bet'

		, deleteBetConfirmationLabel: 'Match and Bets: Delete bet confirmation: Delete bet?'

		, actionAllMatchBet: 'Portal page / Matches / Menu: All bets'

		, actionStandOffHistory: 'Portal page / Matches / Menu: StandOff history'

		, footer_YourBetLabel: 'Match and Bets / Footer: Your bet'
		, footer_NoBetYetLabel: 'Match and Bets / Footer: no bet yet'
		, footer_BettingFinishedLabel: 'Match and Bets / Footer: betting finished'
		, footer_MatchFinishedLabel: 'Match and Bets / Footer: Match finished'
	} );

	var MatchesView = ConfigurableView.extend( {

		renderInnerView: function ( filter ) {
			this.filter = filter;

			this.currentUser = this.options.currentUser;

			this.listenToOnce( this.model, 'sync', this._renderCupMatches );
			this.model.refresh( filter );
		},

		getIcon: function() {
			return 'fa-futbol-o';
		},

		_renderCupMatches: function() {

			var el = this.$( this.windowBodyContainerSelector );
			el.empty();

			var self = this;
			this.model.forEach( function( matchBet ) {
				self._renderEntry( matchBet, el );
			});

			this.trigger( 'inner-view-rendered' );
		},

		_renderEntry: function ( model, el ) {

			var view = new MatchView( {
				model: model
				, categories: this.categories
				, cups: this.cups
				, teams: this.teams
				, filter: this.filter
				, currentUser: this.currentUser
			} );

			return el.append( view.render().$el );
		}
	});

	var MatchView = Backbone.View.extend( {

		events: {
			'click .button-bet-match, .js-menu-match-bet-add': '_onBetButtonClick'
			, 'click .button-bet-save, .js-menu-match-bet-save': '_onSaveBetButtonClick'
			, 'click .button-bet-discard, .js-menu-match-bet-cancel-editing': '_onDiscardButtonClick'
			, 'click .button-edit-bet, .js-menu-match-bet-edit': '_onBetEditButtonClick'
			, 'click .button-delete-bet, .js-menu-match-bet-delete': '_onBetDeleteButtonClick'
		},

		initialize: function ( options ) {

			this.filter = options.filter;
			this.currentUser = options.currentUser;

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
			this._fadeIn();

			this._renderDropDownMenuItems();

			var match = this.model.get( 'match' );
			if ( match.matchFinished ) {
				this.$( '.js-panel-footer' ).html( this._renderIcon( 'fa-flag-checkered', translator.footer_MatchFinishedLabel, false ) );
			}

			var isFilterByAnotherUserBets = this.filter.userId && this.currentUser.userId != this.filter.userId;
			if ( isFilterByAnotherUserBets ) {

				var userBet = this.model.get( 'bet' );

//				this.$( '.js-panel-footer' ).append( this._renderIcon( 'fa-money', translator.footer_YourBetLabel, false ) );
				this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 match-bet-score text-right'>" + userBet.score1 + "</div>" );
				this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 match-bet-score'>" + userBet.score2 + "</div>" );

				return this;
			}

			var bet = this.model.get( 'bet' );
			if( bet == null ) {

				if ( this.model.isBettingAllowed() ) {
					this._setMatchContainerClass( 'panel-warning' );
					this.$( '.js-panel-footer' ).append( this._renderIcon( 'fa-money', translator.footer_NoBetYetLabel, true ) );
					this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-default fa fa-money button-bet-match' title='" + translator.actionMatchBetAdd + "'></button>" );
				} else if( ! match.matchFinished ) {
					this.$( '.js-panel-footer' ).append( this._renderIcon( 'fa-flag-o', translator.footer_BettingFinishedLabel, false ) );
				}

				return this;
			}

			var betUserId = bet.user.userId;

			this._setMatchContainerClass( 'panel-success' );

			if ( ! match.matchFinished ) {
				this.$( '.js-panel-footer' ).append( this._renderIcon( 'fa-money', translator.footer_YourBetLabel, false ) );
			}
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 match-bet-score text-right'>" + bet.score1 + "</div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 match-bet-score'>" + bet.score2 + "</div>" );

			if ( ! match.matchFinished ) {
				this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-default fa fa-edit button-edit-bet' title='" + translator.actionMatchBetEdit + "'></button>" );
				this.$( '.bet-buttons-cell' ).append( "<button class='btn btn-default fa fa-close button-delete-bet' title='" + translator.actionMatchBetDelete + "'></button>" );
			}

			return this;
		},

		renderBetForm: function () {

			this.$el.html( templateMatch( this._getViewOptions() ) );
			this._fadeIn();

			this._renderDropDownMenuItems();

			var bet = this.model.get( 'bet' );
			var bet1 = bet != null ? bet.score1 : 0;
			var bet2 = bet != null ? bet.score2 : 0;

			this._setMatchContainerClass( 'panel-danger' );

			this.$( '.js-panel-footer' ).append( this._renderIcon( 'fa-money', translator.footer_YourBetLabel, false ) );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3 text-right'><input class='form-control' id='score1' name='score1' type='number' value='" + bet1 + "'></div>" );
			this.$( '.js-panel-footer' ).append( "<div class='col-lg-3'><input class='form-control' id='score2' name='score2' type='number' value='" + bet2 + "'></div>" );

			this.$( '.bet-buttons-cell' ).html( "<button class='btn btn-primary fa fa-save button-bet-save' title='" + translator.actionMatchBetSave + "'></button>" );
			this.$( '.bet-buttons-cell' ).append( "<button class='btn btn-default fa fa-close button-bet-discard' title='" + translator.actionCancelBetEditing + "'></button>" );

			return this;
		},

		_renderIcon: function( icon, title, disabled ) {
			return "<div class='col-lg-3 fa " + icon + " fa-2x' title='" + title + "' " + ( disabled ? "style='opacity: 0.2;'" : "")  + "></div>";
		},

		_fadeIn: function() {
			this.$( '.js-match-container' ).fadeIn( 500, "swing" );
		},

		_renderDropDownMenuItems: function() {
			var menuItems = [
				{ selector: 'js-menu-standoff-history', icon: 'fa fa-calendar', link: '#', text: translator.actionStandOffHistory }
				, { selector: 'divider' }
				, { selector: 'js-menu-all-match-bets', icon: 'fa fa-money', link: '#', text: translator.actionAllMatchBet }
			];

			var match = this.model.get( 'match' );

			var bet = this.model.get( 'bet' );
			var isBetEditingMode = this.model.isBetMode();

			if ( ! match.matchFinished ) {

				menuItems.push( { selector: 'divider' } );

				if ( isBetEditingMode ) {
					menuItems.push( { selector: 'js-menu-match-bet-save', icon: 'fa fa-edit', link: '#', text: translator.actionMatchBetSave } );
					menuItems.push( { selector: 'js-menu-match-bet-cancel-editing', icon: 'fa fa-close', link: '#', text: translator.actionCancelBetEditing} );
				}

				if ( bet == null ) {
					if ( this.model.isBettingAllowed() && !isBetEditingMode ) {
						menuItems.push( { selector: 'js-menu-match-bet-add', icon: 'fa fa-plus', link: '#', text: translator.actionMatchBetAdd } );
					}
				} else {
					if ( ! isBetEditingMode ) {
						menuItems.push( { selector: 'js-menu-match-bet-edit', icon: 'fa fa-edit', link: '#', text: translator.actionMatchBetEdit } );
						menuItems.push( { selector: 'js-menu-match-bet-delete', icon: 'fa fa-close', link: '#', text: translator.actionMatchBetDelete } );
					}
				}
			}

			mainMenu( menuItems, 'fa-list', this.$( '.js-match-drop-down-menu') );
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

			if ( ! confirm( translator.deleteBetConfirmationLabel ) ) {
				return;
			}

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