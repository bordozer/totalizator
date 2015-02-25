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

			var view = this.renderPage();

			this.renderMenu();

			return view;
		},

		renderPage: function() {

		},

		renderMenu: function() {
			mainMenu( this.menus(), this.$( '.main-menu-container') );
		}
	});
} );
