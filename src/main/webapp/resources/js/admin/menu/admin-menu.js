define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-menu-model' );
	var View = require( './admin-menu-view' );

	function init( menus, container ) {
		var model = new Model.AdminMenuModel();
		var view = new View.AdminMenuView( { model: model, el: container, menus: menus } );
	}

	return init;
});
