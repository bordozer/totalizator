define( function( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './points-calculation-strategies-widget-model' );
	var View = require( './points-calculation-strategies-widget-view' );

	function init( container, options ) {

		var model = new Model.List( { options: options } );

		var view = new View( {
			model: model
			, el: container
		} );

		return {

			view: function() {
				return view;
			}
		}
	}

	return init;
});