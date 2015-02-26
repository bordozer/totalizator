define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './cup-entry-model' );
	var View = require( './cup-entry-view' );

	function init( container ) {
		var model = new Model.AdminModel();
		var view = new View.AdminView( { model: model, el: container } );
	}

	return init;
});
