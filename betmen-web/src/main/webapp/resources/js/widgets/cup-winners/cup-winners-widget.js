define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './cup-winners-widget-model' );
	var View = require( './cup-winners-widget-view' );

	function init( container, options ) {
		var model = new Model( { options: options } );
		var view = new View( { model: model, el: container, options: options } );
	}

	return init;
} );
