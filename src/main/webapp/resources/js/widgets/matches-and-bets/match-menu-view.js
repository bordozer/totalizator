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
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var betsCount = this.model.get( 'betsCount' ); // TODO: load 'fresh' data each rendering
			var match = this.model.get( 'match' );

			var menuItems = [
				{
					selector: 'js-menu-standoff-history',
					icon: 'fa fa-calendar',
					link: '/totalizator/teams/standoff/' + match.team1.teamId + '/vs/' + match.team2.teamId + '/',
					text: translator.actionStandOffHistory
				}
				, {selector: 'divider'}
				, {
					selector: 'js-menu-all-match-bets',
					icon: 'fa fa-money',
					link: '/totalizator/matches/' + match.matchId + '/bets/',
					text: translator.actionAllMatchBet + ' ( ' + betsCount + ' )'
				}
			];

			var bet = this.model.get( 'bet' );
			var isBettingAllowed = this.model.isBettingAllowed();
			var isBetEditingMode = this.model.isBetMode();

			if ( isBettingAllowed ) {

				menuItems.push( {selector: 'divider'} );

				if ( isBetEditingMode ) {
					menuItems.push( {
						selector: 'js-menu-match-bet-save',
						icon: 'fa fa-edit',
						link: '#',
						text: translator.actionMatchBetSave
					} );
					menuItems.push( {
						selector: 'js-menu-match-bet-cancel-editing',
						icon: 'fa fa-close',
						link: '#',
						text: translator.actionCancelBetEditing
					} );
				}

				if ( bet == null ) {
					if ( this.model.isBettingAllowed() && !isBetEditingMode ) {
						menuItems.push( {
							selector: 'js-menu-match-bet-add',
							icon: 'fa fa-plus',
							link: '#',
							text: translator.actionMatchBetAdd
						} );
					}
				} else {
					if ( !isBetEditingMode ) {
						menuItems.push( {
							selector: 'js-menu-match-bet-edit',
							icon: 'fa fa-edit',
							link: '#',
							text: translator.actionMatchBetEdit
						} );
						menuItems.push( {
							selector: 'js-menu-match-bet-delete',
							icon: 'fa fa-recycle',
							link: '#',
							text: translator.actionMatchBetDelete
						} );
					}
				}
			}

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