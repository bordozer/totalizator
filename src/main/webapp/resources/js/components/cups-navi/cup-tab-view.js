define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		cupTabMenu_Card: 'card'
		, cupTabMenu_Matches: 'Matches'
		, cupTabMenu_Bets: 'Cup bets'
		, cupTabMenu_WinnersBets: 'Cup result bets'
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.selectedCupId = options.selectedCupId;
			this.cup = options.cup;
		},

		render: function () {

			var buttonText = this.cup.category.categoryName + ': ' + this.cup.cupName;

			var options = {
				menus: this._cupTabMenus()
				, menuButtonIcon: ''
				, menuButtonImage: this.cup.logoUrl
				, menuButtonText: ' ' + buttonText
				, menuButtonHint: buttonText
				, cssClass: this.cup.cupId != this.selectedCupId ? 'btn-default' : 'btn-info'
			};

			mainMenu( options, this.$el );
		},

		_cupTabMenus: function () {

			var menus = [];

			menus.push( {
				selector: 'js-matches'
				, icon: 'fa fa-futbol-o'
				, link: '/totalizator/cups/' + this.cup.cupId + '/'
				, text: this.cup.category.categoryName + ': ' + this.cup.cupName + ' - ' + translator.cupTabMenu_Card
			} );

			menus.push( { selector: 'divider' } );

			menus.push( {
				selector: 'js-matches'
				, icon: 'fa fa-futbol-o'
				, link: '/totalizator/cups/' + this.cup.cupId + '/matches/'
				, text: translator.cupTabMenu_Matches
			} );

			menus.push( { selector: 'divider' } );

			menus.push( {
				selector: 'js-matches'
				, icon: 'fa fa-futbol-o'
				, link: '/totalizator/cups/' + this.cup.cupId + '/bets/'
				, text: translator.cupTabMenu_Bets
			} );

			menus.push( {
				selector: 'js-matches'
				, icon: 'fa fa-futbol-o'
				, link: '/totalizator/cups/' + this.cup.cupId + '/winners/bets/'
				, text: translator.cupTabMenu_WinnersBets
			} );

			return menus;
		}
	} );
} );