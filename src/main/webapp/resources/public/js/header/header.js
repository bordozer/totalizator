define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './header-model' );
	var View = require( './header-view' );

	function init( container, subtitle, menuItems ) {
		var model = new Model.HeaderModel();
		var view = new View.HeaderView( { model: model, el: container, subtitle: subtitle, menuItems: menuItems } );
	}

	return init;
});
