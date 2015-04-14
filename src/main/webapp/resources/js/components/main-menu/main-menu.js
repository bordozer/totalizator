define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './main-menu-model' );
	var View = require( './main-menu-view' );

	function init( menus, icon, cssClass, container ) {

		var model = new Model.MainMenuModel();
		var view = new View.MainMenuView( { model: model, el: container, icon: icon, menus: menus, cssClass: cssClass } );

		return view.render();
	}

	return init;
});
