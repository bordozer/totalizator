define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-model' );
	var View = require( './user-statistics-widget-view' );

	var FilterModel = require( 'js/components/widget-configurable/filter/matches-filter-model' );

	function init( container, options ) {

		var model = new Model.MatchesModel();

		var filterModel = new FilterModel( options.filter );

		var configurableOptions = {
			model: model
			, filterModel: filterModel
			, el: container
			, menuItems: []
			, currentUser: options.currentUser
		};

		var view = new View( configurableOptions );
	}

	return init;
} );
