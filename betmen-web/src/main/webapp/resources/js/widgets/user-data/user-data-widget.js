define( function ( require ) {

	'use strict';

	var Model = require( './user-data-widget-model' );
	var View = require( './user-data-widget-view' );

	function init( container, options ) {
		var model = new Model( { options: options } );
		var view = new View( { model: model, el: container, options: options } );
	}

	return init;
} );

