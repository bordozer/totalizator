define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var pageHeader = require( 'js/components/header/header' );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var template = _.template( require( 'text!./templates/base-page-template.html' ) );

	var app = require( 'app' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		mainMenuLabel: 'Main menu'
	} );

	return Backbone.View.extend( {

		constructor: function ( options ) {
			this.events = _.extend( this.builtinEvents, this.events );

			this.options = options.options;

			this.options.currentUser = app.currentUser(); // TODO: for backward compatibility

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
			this.headerView = pageHeader( this.$( '.header-container'), { breadcrumbs: this.breadcrumbs } ).view();
		},

		_renderMenu: function() {
			var options = {
				menus: this.mainMenuItems()
				, menuButtonIcon: 'fa-list-alt'
				, menuButtonText: ''
				, menuButtonHint: translator.mainMenuLabel
				, cssClass: 'btn-default'
			};
			mainMenu( options, this.$( '.js-main-menu-container') );
		},

		mainMenuItems: function() {
			return [];
		},

		logout: function() {
			service.logout();
		}
	});
} );
