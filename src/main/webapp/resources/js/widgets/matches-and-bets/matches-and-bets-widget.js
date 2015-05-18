define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './matches-and-bets-widget-model' );
	var View = require( './matches-and-bets-widget-view' );
	var ViewCompact = require( './matches-and-bets-widget-vew-compact' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		switchViewsLabel: 'Switch match and bets views'
	} );

	function init( container, options ) {

		var model = new Model.MatchesModel();

		var modeMenu = { selector: 'js-switch-views', icon: 'fa fa-retweet', link: '#', text: translator.switchViewsLabel };
		options.menuItems.push( modeMenu );

		var settings = options.settings;
		var matchesAndBetOptions = {
			model: model
			, el: container
			, settings: {
				userId: settings.userId
				, categoryId: settings.categoryId
				, cupId: settings.cupId
				, teamId: settings.teamId
				, team2Id: settings.team2Id
				, filterByDate: settings.filterByDate
				, filterByDateEnable: settings.filterByDateEnable
				, showFutureMatches: settings.showFutureMatches
				, showFinished: settings.showFinished
			}
			, menuItems: options.menuItems
			, currentUser: options.currentUser
		};

		if ( options.isCompactView ) {
			new ViewCompact( matchesAndBetOptions );
		} else {
			new View.MatchesView( matchesAndBetOptions );
		}
	}

	return init;
});
