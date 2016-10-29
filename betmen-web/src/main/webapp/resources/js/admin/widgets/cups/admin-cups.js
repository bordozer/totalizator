define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-cups-model' );
	var View = require( './admin-cups-view' );

	function init( container ) {
		var model = new Model.CupsModel();
		var view = new View.CupsView( { model: model, el: container } );
	}

	return init;
});
