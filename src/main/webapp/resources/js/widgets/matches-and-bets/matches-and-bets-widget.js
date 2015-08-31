define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './matches-and-bets-widget-model' );
	var FilterModel = require( 'js/components/widget-matches-and-bets/filter/matches-filter-model' );

	var View = require( './matches-and-bets-widget-view' );

	function createView( model, container, filterModel, options ) {

		var matchesAndBetOptions = {
			model: model
			, filterModel: filterModel
			, el: container
			, currentUser: options.currentUser
			, viewMode: options.viewMode
			, initialViewMode: options.initialViewMode
		};

		return new View( matchesAndBetOptions );
	}

	return function( container, options ) {

		var model = new Model.MatchesModel();
		var filterModel = new FilterModel( options.filter );
		var initialViewMode = options.viewMode;

		var view = null;

		var render = _.bind( function( renderOptions ) {

			options.filter = renderOptions.filter;
			options.viewMode = renderOptions.viewMode;
			options.initialViewMode = initialViewMode;

			if ( view ) {
				view.remove();
			}

			var el = $( '<div></div>' );
			container.html( el );

			view = createView( model, el, filterModel, options );

			view.on( 'events:switch_view_mode', render, this, options );
		}, this );

		render( { filter: options.filter, viewMode: options.viewMode } );
	}
});
