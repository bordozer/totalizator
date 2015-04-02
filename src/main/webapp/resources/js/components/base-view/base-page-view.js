define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var pageHeader = require( 'public/js/header/header' );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var template = _.template( require( 'text!./templates/base-page-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	return Backbone.View.extend( {

		constructor: function ( options ) {
			this.events = _.extend( this.builtinEvents, this.events );

			this.breadcrumbs = options.breadcrumbs;
			this.options = options.options;
			this.bodyRenderer = options.bodyRenderer;

			Backbone.View.apply( this, [ options ] );
		},

		render: function() {

			this.$el.html( template( {
			 } ) );

			this._renderHeader();

			this._renderMenu();

			this.renderBody();

			return this;
		},

		renderBody: function() {
			this.bodyView = this.bodyRenderer( this.$( '.body-container' ), this.options ).view();
		},

		_renderHeader: function() {
			pageHeader( this.$( '.header-container'), this.breadcrumbs );
		},

		_renderMenu: function() {
			mainMenu( this.mainMenuItems(), 'fa-list-alt', this.$( '.js-main-menu-container') );
		},

		mainMenuItems: function() {
			return [];
		},

		logout: function() {
			service.logout();
		}
	});
} );
