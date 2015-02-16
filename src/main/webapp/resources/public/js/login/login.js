define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './login-model' );
	var View = require( './login-view' );

	function init( container ) {
		var model = new Model.LoginModel();
		var view = new View.LoginView( { model: model, el: container } );
	}

	return init;
});