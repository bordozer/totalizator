define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './cup-model.js' );
	var View = require( './cup-view.js' );

	function init( container ) {
		var model = new Model.CupsModel();
		var view = new View.CupsView( { model: model, el: container } );
	}

	return init;
});
