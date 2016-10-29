define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './cup-users-scores-widget-model' );
	var View = require( './cup-users-scores-widget-view' );

	function init( container, options ) {

		var model = new Model( { cup: options.cup } );
		var view = new View( {
			model: model
			, el: container
			, user: options.user
		} );
	}

	return init;
} );
