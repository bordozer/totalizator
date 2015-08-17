define( function( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './admin-matches-page-model' );
	var View = require( './admin-matches-page-view' );

	function init( container, options ) {

		var model = new Model( { options: options } );
		var view = new View( { model: model, el:container, options: options } );

		return {

			view: function() {
				return view;
			}
		}
	}

	return init;
});

