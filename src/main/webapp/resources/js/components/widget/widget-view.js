define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/widget-template.html' ) );

	var mainMenu = require( 'js/components/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		loadingLabel: 'Loading...'
		, menuItemRefreshLabel: 'Menu item: Refresh'
		, widgetMenuHint: 'Widget menu'
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
			this.customMenuItems = options.menuItems;

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

		getCupTitle: function( cup, title ) {
			return cup.category.categoryName + ":<strong>" + " <a class='text-info' href='/totalizator/cups/" + cup.cupId + "/'>" +  cup.cupName + "</a></strong> - " +  title;
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

		addCustomMenuItems: function( menuItems ) {
			this.customMenuItems = this.getCustomMenuItems().concat( menuItems );
		},

		getCustomMenuItems: function() {

			if ( this.customMenuItems && this.customMenuItems.length > 0 ) {
				return this.customMenuItems;
			}

			return [];
		},

		footerText: function( text ) {
			this.$( '.js-footer' ).html( text );
		},

		_renderDropDownMenu: function() {

			var menuItems = this.menuItems.concat( this.getCustomMenuItems() );

			var options = {
				menus: menuItems
				, menuButtonIcon: this.getIcon()
				, menuButtonText: ''
				, menuButtonHint: translator.widgetMenuHint
				, cssClass: 'btn-default'
			};
			mainMenu( options, this.$( '.js-window-drop-down-menu') );

			var customButtons = _.filter( this.menuItems, function( menu ) {
				return menu.button;
			});

			var menuContainer = this.$( '.js-custom-buttons' );
			_.each( customButtons, function( menu ) {
				menuContainer.append( "<button class='btn btn-default " + menu.selector + " " + menu.icon + "' title='" + menu.text + "'></button>" );
			});
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
