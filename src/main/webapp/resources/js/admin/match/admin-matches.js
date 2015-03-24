define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var ConfigurableView = require( 'js/components/configurable-view/configurable-view' );

	var Model = require( './admin-matches-model' );
	var View = require( './admin-matches-view' );

	function init( container ) {

		var matchesModel = new Model.MatchesModel();
		var matchesView = new View.MatchesView( {
			model: matchesModel
		} );

		var configurableView = new ConfigurableView( {
			el: container
			, view: matchesView
			, settings: {
				categoryId: 0
				, cupId: 0
			}
		} );

		return {

			view: function() {
				return configurableView;
			}
		}
	}

	return init;
});
