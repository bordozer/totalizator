define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var ConfigurableView = require( 'js/components/configurable-view/configurable-view' );

	var Model = require( './match-model' );
	var View = require( './match-view' );

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
