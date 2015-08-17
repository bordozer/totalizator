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
			return "<small><a href='/totalizator/categories/" + cup.category.categoryId + "/'>" +  cup.category.categoryName + "</a></small>"
					+ ":<small><strong>" + " <a class='text-info' href='/totalizator/cups/" + cup.cupId + "/'>" +  cup.cupName + "</a></strong></small> - " +  title;
		},

		getTitleHint: function() {
			return '';
		},

		getIcon: function() {
			return 'fa-windows';
		},

		getPictureURL: function() {
			return '';
		},

		renderBody: function() {
			// render custom widget context here
		},

		setBody: function( context ) {
			this.$( this.windowBodyContainerSelector ).html( context );
		},

		showProgress: function() {
			var el = this._getIconEl();
			el.html( '' );
			el.removeClass( this.getIcon() );
			el.addClass( this.progressIcon );
		},

		hideProgress: function() {
			var el = this._getIconEl();
			el.html( '' );
			el.removeClass( this.progressIcon );

			if ( this.getPictureURL() ) {
				el.html( "<img src='" + this.getPictureURL() + "' width='32' height='32' style='margin-top: -5px;'>" );
			} else {
				el.addClass( this.getIcon() );
			}
		},

		getCustomMenuItems: function() {
			return [];
		},

		footerText: function( text ) {
			this.$( '.js-footer' ).html( text );
		},

		_renderDropDownMenu: function() {

			var menuItems =  [
				{ selector: 'js-menu-refresh', icon: 'fa fa-refresh', link: '#', text: translator.menuItemRefreshLabel }
			];

			var customMenuItems = this.getCustomMenuItems();
			if ( customMenuItems.length > 0 ) {
				menuItems = menuItems.concat( [ { selector: 'divider' } ] );
			}

			menuItems = menuItems.concat( customMenuItems );

			var options = {
				menus: menuItems
				, menuButtonIcon: this.getIcon()
				//, menuButtonImage: this.getPictureURL()
				, menuButtonText: ''
				, menuButtonHint: translator.widgetMenuHint
				, cssClass: 'btn-default'
			};
			mainMenu( options, this.$( '.js-window-drop-down-menu') );

			var customButtons = _.filter( menuItems, function( menu ) {
				return menu.button;
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
