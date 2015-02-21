define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './category-model.js' );
	var View = require( './category-view.js' );

	function init( container ) {
		var model = new Model.CategoriesModel();
		var view = new View.CategoryView( { model: model, el: container } );
	}

	return init;
});
