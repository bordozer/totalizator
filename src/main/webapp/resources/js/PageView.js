define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var mainMenu = require( 'js/main-menu/main-menu' );

	return Backbone.View.extend( {

		initialize: function( options ) {

		},

		render: function() {

			var view = this.renderBody();

			this.renderMenu();

			return view;
		},

		renderBody: function() {

		},

		renderMenu: function() {
			mainMenu( this.mainMenuItems(), this.$( '.main-menu-container') );
		},

		mainMenuItems: function() {
			return [];
		}
	});
} );
