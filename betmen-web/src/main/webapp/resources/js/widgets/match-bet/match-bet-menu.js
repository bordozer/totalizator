define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		actionMatchBetAdd: 'Match and Bets: Add bet'
		, actionMatchBetEdit: 'Match and Bets: Edit bet'
		, actionMatchBetSave: 'Match and Bets: save bet'
		, actionCancelBetEditing: 'Match and Bets: Cancel bet editing'
		, actionMatchBetDelete: 'Match and Bets: Delete bet'
		, actionAllMatchBet: 'Portal page / Matches / Menu: All bets'
		, actionStandOffHistory: 'Teams standoff history'
		, teamMatchesLabel: 'matches'
		, menuHint: "Match menu"
		, closeMatchInfo: "Close match info"
		, matchDescription: 'Match description'
	} );

	var MODE_INFO = 1;
	var MODE_EDIT = 2;
	var MODE_DESCRIPTION = 3;

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	function showButton( mode, matchViewMode ) {
		return mode == MODE_EDIT
				|| mode == MODE_DESCRIPTION
				|| matchViewMode == VIEW_MODE_BET
				;
	}

	return {

		getMenuItems: function( matchBet, mode, matchViewMode ) {

			var result = this.getCommonMenuItems( matchBet );

			var modeDependantMenuItems = this._getModeDependantMenuItems( matchBet, mode, matchViewMode );
			if ( modeDependantMenuItems.length > 0 ) {
				result.push( {selector: 'divider'} );
			}

			return result.concat( modeDependantMenuItems );
		},

		getCommonMenuItems: function( matchBet ) {

			var match = matchBet.match;
			var betsCount = service.getBetsCount( match.matchId );

			return [
				{
					selector: 'js-menu-standoff-history',
					icon: 'fa fa-calendar',
					link: '/betmen/teams/standoff/' + match.team1.teamId + '/vs/' + match.team2.teamId + '/',
					text: translator.actionStandOffHistory
				}
				, { selector: 'divider' }
				, {
					selector: 'js-menu-team1-matches',
					icon: 'fa fa-futbol-o',
					link: '/betmen/cups/' + match.cup.cupId + '/matches/teams/' + match.team1.teamId + '/',
					text: match.cup.cupName + ' / ' + match.team1.teamName + ' - ' + translator.teamMatchesLabel
				}
				, {
					selector: 'js-menu-team1-matches',
					icon: 'fa fa-futbol-o',
					link: '/betmen/cups/' + match.cup.cupId + '/matches/teams/' + match.team2.teamId + '/',
					text: match.cup.cupName + ' / ' + match.team2.teamName + ' - ' + translator.teamMatchesLabel
				}
				, { selector: 'divider' }
				, {
					selector: 'js-menu-all-match-bets',
					icon: match.showAnotherBets ? 'fa fa-money' : 'fa fa-lock',
					link: '/betmen/matches/' + match.matchId + '/bets/',
					text: translator.actionAllMatchBet + ' ( ' + betsCount + ' )'
				}
			];
		},

		_getModeDependantMenuItems: function( matchBet, mode, matchViewMode ) {

			if ( mode == MODE_INFO ) {
				return this._getMatchInfoMenuItems( matchBet, mode, matchViewMode );
			}

			if ( mode == MODE_EDIT ) {
				return this._getBetEditMenuItems( matchBet, mode, matchViewMode );
			}

			if ( mode == MODE_DESCRIPTION ) {
				return this._getMatchDescriptionMenuItems( matchBet, mode, matchViewMode );
			}
		},

		_getMatchInfoMenuItems: function( matchBet, mode, matchViewMode ) {

			var match = matchBet.match;
			var bet = matchBet.bet;
			var isBettingAllowed = matchBet.bettingAllowed;

			var button = showButton( mode, matchViewMode );

			var menuItems = [];
			if ( isBettingAllowed ) {
				if ( bet == null && isBettingAllowed ) {
					menuItems.push( {
						selector: 'js-menu-match-bet-add',
						icon: 'fa fa-money',
						link: '#',
						text: translator.actionMatchBetAdd
						, button: button
					} );
				}

				if( bet != null ) {
					menuItems.push( {
						selector: 'js-menu-match-bet-edit',
						icon: 'fa fa-edit',
						link: '#',
						text: translator.actionMatchBetEdit
						, button: button
					} );
					menuItems.push( {
						selector: 'js-menu-match-bet-delete',
						icon: 'fa fa-recycle',
						link: '#',
						text: translator.actionMatchBetDelete
					} );
				}
			}

			if ( match.description ) {
				menuItems.push( {selector: 'divider'} );
				menuItems.push( { selector: 'js-menu-match-description', icon: 'fa fa-info', link: '#', text: translator.matchDescription, button: button } );
			}

			return menuItems;
		},

		_getBetEditMenuItems: function( matchBet, mode, matchViewMode ) {

			var match = matchBet.match;
			var bet = matchBet.bet;
			var isBettingAllowed = matchBet.bettingAllowed;

			var button = showButton( mode, matchViewMode );

			var menuItems = [];
			if ( isBettingAllowed ) {
				menuItems.push( {
					selector: 'js-menu-match-bet-save'
					, icon: 'fa fa-save'
					, link: '#'
					, text: translator.actionMatchBetSave + ' ( Enter )'
					, button: button
					, cssClass: 'btn-primary'
				} );
				menuItems.push( {
					selector: 'js-menu-match-bet-cancel-editing'
					, icon: 'fa fa-close'
					, link: '#'
					, text: translator.actionCancelBetEditing
					, button: button
				} );
			}

			if ( match.description ) {
				menuItems.push( {selector: 'divider'} );
				menuItems.push( { selector: 'js-menu-match-description', icon: 'fa fa-info', link: '#', text: translator.matchDescription, button: button } );
			}

			return menuItems;
		},

		_getMatchDescriptionMenuItems: function( matchBet, mode, matchViewMode ) {

			var button = showButton( mode, matchViewMode );

			return [
				{ selector: 'js-close-match-description'
					, icon: 'fa fa-close'
					, link: '#'
					, text: translator.closeMatchInfo
					, button: button
				}
			];
		}
	}
} );
