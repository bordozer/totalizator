define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var ModelExpanded = require( './matches-and-bets-widget-model' );

	var FilterModel = require( 'js/components/widget-matches-and-bets/filter/matches-filter-model' );

	var View = require( './matches-and-bets-widget-view' );

	function createView( model, container, filterModel, options ) {

		var matchesAndBetOptions = {
			model: model
			, filterModel: filterModel
			, el: container
			, currentUser: options.currentUser
			, matchesAndBetsViewMode: options.matchesAndBetsViewMode
			, matchViewMode: options.matchViewMode
			, initialMatchViewMode: options.initialMatchViewMode
		};

		return new View( matchesAndBetOptions );
	}

	return function( container, options ) {

		var model = new ModelExpanded.MatchesModel();
		var filterModel = new FilterModel( options.filter );
		var initialMatchViewMode = options.matchViewMode;

		var view = null;

		var render = _.bind( function( renderOptions ) {

			options.filter = renderOptions.filter;
			options.matchesAndBetsViewMode = renderOptions.matchesAndBetsViewMode;
			options.matchViewMode = renderOptions.matchViewMode;
			options.initialMatchViewMode = initialMatchViewMode;

			if ( view ) {
				view.remove();
			}

			var el = $( '<div></div>' );
			container.html( el );

			view = createView( model, el, filterModel, options );

			view.on( 'events:switch_view_mode', render, this, options );
		}, this );

		render( { filter: options.filter, matchViewMode: options.matchViewMode, matchesAndBetsViewMode: options.matchesAndBetsViewMode } );
	}
});
