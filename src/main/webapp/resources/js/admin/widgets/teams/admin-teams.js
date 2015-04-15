define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-teams-model' );
	var View = require( './admin-teams-view' );

	function init( container ) {
		var model = new Model.TeamsModel();
		var view = new View.TeamsView( { model: model, el: container } );
	}

	return init;
});
