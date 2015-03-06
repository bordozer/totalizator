define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './bets-model' );
	var View = require( './bets-view' );

	function init( container ) {
		var model = new Model.BetsModel();
		var view = new View.PortalPageView( { model: model, el: container } );
	}

	return init;
});
