define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-categories-model.js' );
	var View = require( './admin-categories-view.js' );

	function init( container ) {
		var model = new Model.CategoriesModel();
		var view = new View.CategoriesView( { model: model, el: container } );
	}

	return init;
});
