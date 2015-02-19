define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './portal-model' );
	var View = require( './portal-view' );

	function init( container ) {
		var model = new Model.PortalPageModel();
		var view = new View.PortalPageView( { model: model, el: container } );

		view.render();
	}

	return init;
});
