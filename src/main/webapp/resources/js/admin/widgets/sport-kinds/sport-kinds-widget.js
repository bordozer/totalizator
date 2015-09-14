define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './sport-kinds-widget-model' );
	var View = require( './sport-kinds-widget-view' );

	function init( container, options ) {
		var model = new Model( { options: options } );
		var view = new View( { model: model, el: container, options: options } );
	}

	return init;
} );