define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

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
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.menuItems = options.menuItems || [];
			this.render();
		},

		render: function () {

			var betsCount = this.model.get( 'betsCount' ); // TODO: load 'fresh' data each rendering
			var match = this.model.get( 'match' );

			var commonMenuItems = [
				{
					selector: 'js-menu-standoff-history',
					icon: 'fa fa-calendar',
					link: '/totalizator/teams/standoff/' + match.team1.teamId + '/vs/' + match.team2.teamId + '/',
					text: translator.actionStandOffHistory
				}
				, {selector: 'divider'}
				, {
					selector: 'js-menu-team1-matches',
					icon: 'fa fa-futbol-o',
					link: '/totalizator/cups/15/matches/teams/' + match.team1.teamId + '/',
					text: match.cup.cupName + ' / ' + match.team1.teamName + ' - ' + translator.teamMatchesLabel
				}
				, {
					selector: 'js-menu-team1-matches',
					icon: 'fa fa-futbol-o',
					link: '/totalizator/cups/15/matches/teams/' + match.team2.teamId + '/',
					text: match.cup.cupName + ' / ' + match.team2.teamName + ' - ' + translator.teamMatchesLabel
				}
				, {selector: 'divider'}
				, {
					selector: 'js-menu-all-match-bets',
					icon: 'fa fa-money',
					link: '/totalizator/matches/' + match.matchId + '/bets/',
					text: translator.actionAllMatchBet + ' ( ' + betsCount + ' )'
				}
			];

			if ( this.menuItems.length > 0 ) {
				commonMenuItems.push( {selector: 'divider'} );
			}

			var menuItems = commonMenuItems.concat( this.menuItems );

			var options = {
				menus: menuItems
				, menuButtonIcon: 'fa-list'
				, menuButtonText: ''
				, menuButtonHint: translator.menuHint
				, cssClass: 'btn-default'
			};

			mainMenu( options, this.$el );


			//var data = _.extend( {}, this.model.toJSON(), {translator: translator} );
			//this.$el.html( template( data ) );

			return this;
		}
	} );
} );