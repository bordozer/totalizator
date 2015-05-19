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

	var view = null;

	function createView( model, container, options ) {

		var filter = options.filter;
		var matchesAndBetOptions = {
			model: model
			, el: container
			, settings: {
				userId: filter.userId
				, categoryId: filter.categoryId
				, cupId: filter.cupId
				, teamId: filter.teamId
				, team2Id: filter.team2Id
				, filterByDate: filter.filterByDate
				, filterByDateEnable: filter.filterByDateEnable
				, showFutureMatches: filter.showFutureMatches
				, showFinished: filter.showFinished
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

		var render = _.bind( function( filter ) {

			options.filter = filter;

			if ( view ) {
				view.remove();
			}

			var el = $( '<div></div>' );
			container.html( el );

			view = createView( model, el, options );

			view.on( 'events:switch_views', render, this, filter );

			options.isCompactView = ! options.isCompactView;
		}, this );

		render( options.filter );
	}

	return init;
});
