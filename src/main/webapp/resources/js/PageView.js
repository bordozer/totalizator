define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var pageHeader = require( 'public/js/header/header' );
	var mainMenu = require( 'js/main-menu/main-menu' );

	return Backbone.View.extend( {

		initialize: function( options ) {

		},

		render: function() {

			var view = this.renderBody();

			this.renderHeader();

			this.renderMenu();

			return view;
		},

		renderHeader: function() {
			pageHeader( this.$( '.header-container'), this.getPageSubTitle() );
		},

		renderMenu: function() {
			mainMenu( this.mainMenuItems(), this.$( '.main-menu-container') );
		},

		renderBody: function() {

		},

		getPageSubTitle: function() {
			return '';
		},

		mainMenuItems: function() {
			return [];
		}
	});
} );
