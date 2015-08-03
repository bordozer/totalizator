define( function( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './points-calculation-strategies-model' );
	var View = require( './points-calculation-strategies-view' );

	function init( container, options ) {

		var model = new Model( { options: options } );

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

