define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './translations-model' );
	var View = require( './translations-view' );

	function init( container ) {
		var model = new Model.TranslationsModel();
		var view = new View.AdminView( { model: model, el: container } );
	}

	return init;
});
