define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-teams-model' );
	var View = require( './admin-teams-view' );

	function init( container ) {
		var model = new Model();
		var view = new View( { model: model, el: container } );
	}

	return init;
});
