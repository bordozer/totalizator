define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './user-data-model' );
	var View = require( './user-data-view' );

	function init( container ) {
		var model = new Model.LoginModel();
		var view = new View.LoginView( { model: model, el: container } );
	}

	return init;
});