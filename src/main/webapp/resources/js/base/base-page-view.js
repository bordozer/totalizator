define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var pageHeader = require( 'public/js/header/header' );

	var mainMenu = require( 'js/main-menu/main-menu' );

	var Template = require( 'text!js/base/templates/base-page-template.html' );

	var Services = require( '/resources/js/services.js' );

	return Backbone.View.extend( {

		basePageTemplate:_.template( Template ),

		constructor: function ( options ) {
			this.events = _.extend( this.builtinEvents, this.events );
			Backbone.View.apply( this, [ options ] );
		},

		render: function() {

			this.$el.html( this.basePageTemplate( {
			 } ) );

			this._renderHeader();

			this._renderMenu();

			this.renderBody( this.$( '.body-container') );

			return this;
		},

		renderBody: function( el ) {

		},

		_renderHeader: function() {
			pageHeader( this.$( '.header-container'), this.getPageSubTitle() );
		},

		_renderMenu: function() {
			mainMenu( this._mainMenuItems(), this.$( '.main-menu-container') );
		},

		getPageSubTitle: function() {
			return '';
		},

		_mainMenuItems: function() {
			return [];
		},

		logout: function() {
			Services.logout();
		}
	});
} );
