define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var templateInfo = _.template( require( 'text!./templates/bet-zone-template.html' ) );
	var templateEdit = _.template( require( 'text!./templates/bet-zone-edit-template.html' ) );

	var pointsStylist = require( 'js/services/points-stylist' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
		, yourBetLabel: 'Your bet'
		, noBetYetLabel: 'Match and Bets / Footer: no bet yet'
		, betOfAnotherUserLabel: 'Bet of another user'
		, bettingFinishedLabel: 'Match and Bets / Footer: betting finished'
		, anotherBetsAreHidden: "Bets of another users will be shown after the match start"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {

			this.matchBet = options.matchBet;
			this.isEdit = options.isEdit;

			this.showBetForUserId = options.showBetForUserId;
			this.currentUser = options.currentUser;

			this.render();
		},

		render: function () {
			if ( this.isEdit ) {
				this._renderEdit();
				return;
			}

			this._renderInfo();
		},

		_renderInfo: function () {

			var data = _.extend( {}, {} );

			this.$el.html( templateInfo( data ) );

			this._renderIcon();
			this._renderBetPoints();
			this._renderUserPoints();
		},

		_renderEdit: function() {

			var bet = this.matchBet.bet;
			var isBet = bet != null;

			var betScore1 = isBet ? bet.score1 : 0;
			var betScore2 = isBet ? bet.score2 : 0;

			var data = _.extend( {}, { betScore1: betScore1, betScore2: betScore2 } );

			this.$el.html( templateEdit( data ) );

			var self = this;
			this.$( 'input' ).keypress( function ( e ) {
				if ( e.which == 13 ) {
					self._saveBet();
				}
			} );

			this.$( '#score1' ).focus().select();
		},

		_renderIcon: function() {

			var match = this.matchBet.match;
			var isMatchFinished = match.matchFinished;

			var bet = this.matchBet.bet;
			var noBet = bet == null;
			var isBettingAllowed = this.matchBet.bettingAllowed;

			if ( isMatchFinished ) {
				this._doRenderIcon( 'fa-flag-checkered', this.matchBet.bettingValidationMessage, false );
				return;
			}

			var isFilterByAnotherUserBets = this.showBetForUserId && this.currentUser.userId != this.showBetForUserId;
			if ( isFilterByAnotherUserBets ) {
				this._doRenderIcon( 'fa-user', translator.betOfAnotherUserLabel, false );
				return;
			}

			if ( noBet && isBettingAllowed ) {
				this._doRenderIcon( match.cup.readyForMatchBets ? 'fa-money' : 'fa-ban', translator.noBetYetLabel, true );
				return;
			}

			if ( noBet && ! isBettingAllowed && ! isMatchFinished ) {
				this._doRenderIcon( 'fa-flag-o', translator.bettingFinishedLabel, false );
				return;
			}

			if ( ! isMatchFinished ) {
				this._doRenderIcon( 'fa-money', translator.yourBetLabel, false );
			}
		},

		_renderBetPoints: function() {

			var match = this.matchBet.match;
			var bet = this.matchBet.bet;

			var noBet = bet == null;
			if ( noBet ) {
				return;
			}

			var score1El = this.$( '.js-bet-zone-bet-points-1' );
			var score2El = this.$( '.js-bet-zone-bet-points-2' );

			var isFilterByAnotherUserBets = this.showBetForUserId && this.currentUser.userId != this.showBetForUserId;
			if ( isFilterByAnotherUserBets && bet.securedBet ) {

				var hiddenScore = "<span class='fa fa-lock fa-2x text-muted' title='" + translator.anotherBetsAreHidden + "'></span>";

				score1El.html( hiddenScore );
				score2El.html( hiddenScore );

				return;
			}

			var betStyled = pointsStylist.styleBetPoints( match, bet );

			score1El.html( bet.score1 );
			score1El.addClass( betStyled.style1 );
			score1El.attr( 'title', betStyled.title );

			score2El.html( bet.score2 );
			score2El.addClass( betStyled.style2 );
			score2El.attr( 'title', betStyled.title );
		},

		_renderUserPoints: function() {
			if ( this.matchBet.points > 0 ) {
				this.$( '.js-bet-zone-points' ).html( '+ ' + this.matchBet.points );
			}
		},

		_doRenderIcon: function( icon, title, disabled ) {
			this.$( '.js-bet-zone-icon' ).html( "<div class='col-xs-3 fa " + icon + " fa-2x' title='" + title + "' " + ( disabled ? "style='opacity: 0.2;'" : "") + " ></div>" );
		}
	} );
} );
