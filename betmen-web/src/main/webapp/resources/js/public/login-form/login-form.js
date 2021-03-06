define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './login-form-model' );
	var View = require( './login-form-view' );

	function init( container ) {
		var model = new Model.LoginFormModel();
		var view = new View.LoginFormView( { model: model, el: container } );
		view.render();
	}

	return init;
});