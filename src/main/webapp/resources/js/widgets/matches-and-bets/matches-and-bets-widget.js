define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './matches-and-bets-widget-model' );
	var FilterModel = require( 'js/components/widget-matches-and-bets/filter/matches-filter-model' );

	var View = require( './matches-and-bets-widget-view' );
	var ViewCompact = require( './matches-and-bets-widget-view-compact' );

	function createView( model, container, filterModel, options ) {

		var matchesAndBetOptions = {
			model: model
			, filterModel: filterModel
			, el: container
			, currentUser: options.currentUser
		};

		if ( options.isCompactView ) {
			return new ViewCompact( matchesAndBetOptions );
		}

		return new View( matchesAndBetOptions );
	}

	return function( container, options ) {

		var model = new Model.MatchesModel();
		var filterModel = new FilterModel( options.filter );

		var view = null;

		var render = _.bind( function( filter ) {

			options.filter = filter;

			if ( view ) {
				view.remove();
			}

			var el = $( '<div></div>' );
			container.html( el );

			view = createView( model, el, filterModel, options );

			view.on( 'events:switch_views', render, this, filter );

			options.isCompactView = ! options.isCompactView;
		}, this );

		render( options.filter );
	}
});
