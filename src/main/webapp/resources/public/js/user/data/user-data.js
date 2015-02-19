define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './user-data-model' );
	var View = require( './user-data-view' );

	function init( container ) {
		var model = new Model.UserDataModel();
		var view = new View.UserDataView( { model: model, el: container } );

		view.render();
	}

	return init;
});