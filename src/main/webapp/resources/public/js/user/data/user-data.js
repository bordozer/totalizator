define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './user-data-model' );
	var View = require( './user-data-view' );

	function init( container, options ) {
		var model = new Model.UserDataModel( { options: options } );
		var view = new View.UserDataView( { model: model, el: container, options: options } );

		view.render();
	}

	return init;
});