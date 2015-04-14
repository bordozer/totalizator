define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/window-template.html' ) );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		loadingLabel: 'Loading...'
		, menuItemRefreshLabel: 'Menu item: Refresh'
	} );

	return Backbone.View.extend( {

		progressIcon: 'fa-spinner fa-spin',
		windowBodyContainerSelector: '.js-window-container',

		builtinEvents: {
			'click .js-menu-refresh': '_onMenuRefreshClick'
		},

		constructor: function ( options ) {

			this.menuItems =  [
				{ selector: 'js-menu-refresh', icon: 'fa fa-refresh', link: '#', text: translator.menuItemRefreshLabel }
			];
			this.addMenuItems( options.menuItems );

			this.events = _.extend( this.builtinEvents, this.events );

			this.on( 'view:render', this.render, this );
			this.on( 'inner-view-rendered', this._onInnerViewRendered, this );

			Backbone.View.apply( this, [ options ] );
		},

		render: function() {

			this.$el.html( template( {
				title: this.getTitle()
				, titleHint: this.getTitleHint()
				, icon: this.getIcon()
				, translator: translator
			} ) );

			this.showProgress();

			this._renderDropDownMenu();

			this.renderBody();

			this.delegateEvents();
		},

		getTitle: function() {
			return '...';
		},

		getTitleHint: function() {

		},

		getIcon: function() {
			return 'fa-windows';
		},

		renderBody: function() {

		},

		setBody: function( context ) {
			this.$( this.windowBodyContainerSelector ).html( context );
		},

		showProgress: function() {
			var el = this._getIconEl();
			el.removeClass( this.getIcon() );
			el.addClass( this.progressIcon );
		},

		hideProgress: function() {
			var el = this._getIconEl();
			el.removeClass( this.progressIcon );
			el.addClass( this.getIcon() );
		},

		addMenuItems: function( menuItems ) {
			if ( menuItems && menuItems.length > 0 ) {
				this.menuItems = this.menuItems.concat( menuItems );
			}
		},

		_renderDropDownMenu: function() {
			mainMenu( this.menuItems, this.getIcon(), 'btn-default', this.$( '.js-window-drop-down-menu') );
		},

		_onInnerViewRendered: function() {
			this.hideProgress();
		},

		_getIconEl: function() {
			return this.$( '.js-window-icon' );
		},

		_onMenuRefreshClick: function() {
			this.render();
		}
	});
} );
