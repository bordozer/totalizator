define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './match-model.js' );
	var View = require( './match-view.js' );

	function init( container ) {
		var model = new Model.MatchesModel();
		var view = new View.MatchesView( { model: model, el: container } );
	}

	return init;
});
