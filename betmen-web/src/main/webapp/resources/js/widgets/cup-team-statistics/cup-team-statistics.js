define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './cup-team-statistics-model' );
	var View = require( './cup-team-statistics-view' );

	function init( container, options ) {
		var model = new Model( { options: options } );
		var view = new View( { model: model, el: container, options: options } );
	}

	return init;
} );
