define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './matches-and-bets-widget-model' );
	var View = require( './matches-and-bets-widget-view' );

	function init( container ) {
		var model = new Model.MatchesModel();
		var view = new View.PortalPageView( { model: model, el: container } );
	}

	return init;
});
