define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './cup-users-scores-model' );
	var View = require( './cup-users-scores-view' );

	function init( cup, container ) {
		var model = new Model( { cup: cup } );
		var view = new View( { model: model, el: container, cup: cup } );
	}

	return init;
} );
