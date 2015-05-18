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

	function createView( renderOptions ) {

		var model = renderOptions.model;
		var container = renderOptions.container;
		var options = renderOptions.options;

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
			return new ViewCompact( matchesAndBetOptions );
		}

		return new View.MatchesView( matchesAndBetOptions );
	}

	function addSwitchViewsMenu( menuItems ) {
		var switchViewsMenu = { selector: 'js-switch-views', icon: 'fa fa-retweet', link: '#', text: translator.switchViewsLabel };
		menuItems.push( switchViewsMenu );
	}

	function init( container, options ) {

		addSwitchViewsMenu( options.menuItems );

		var model = new Model.MatchesModel();

		var renderOptions = {
			model: model
			, container: container
			, options: options
		};

		var render = _.bind( function() {
			createView( renderOptions );
			options.isCompactView = ! options.isCompactView;
		}, this );

		var view = createView( renderOptions );
		options.isCompactView = ! options.isCompactView;

		view.on( 'events:switch_views', render, this );
	}

	return init;
});
