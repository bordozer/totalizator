define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var pageHeader = require( 'js/components/header/header' );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var template = _.template( require( 'text!./templates/base-page-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	return Backbone.View.extend( {

		constructor: function ( options ) {
			this.events = _.extend( this.builtinEvents, this.events );

			this.options = options.options;

			this.currentUser = options.options.currentUser;

			this.breadcrumbs = options.breadcrumbs;
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
			this.bodyView = this.bodyRenderer( this.$( '.js-body-view-container' ), this.options ).view();
		},

		_renderHeader: function() {
			this.headerView = pageHeader( this.$( '.header-container'), { breadcrumbs: this.breadcrumbs, currentUser: this.currentUser } ).view();
		},

		_renderMenu: function() {
			mainMenu( this.mainMenuItems(), 'fa-list-alt', 'btn-default', this.$( '.js-main-menu-container') );
		},

		mainMenuItems: function() {
			return [];
		},

		logout: function() {
			service.logout();
		}
	});
} );
