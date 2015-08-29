define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-matches-widget-model' );
	var View = require( './admin-matches-widget-view' );

	var FilterModel = require( 'js/components/widget-matches-and-bets/filter/matches-filter-model' );

	function init( container, options ) {

		var matchesModel = new Model.MatchesModel( { options: options } );

		var filter = {
			categoryId: options.categoryId
				, cupId: options.cupId
				, showFutureMatches: true
		};
		var filterModel = new FilterModel( filter );

		var matchesView = new View( {
			model: matchesModel
			, el: container
			, filterModel: filterModel
		} );
	}

	return init;
});
