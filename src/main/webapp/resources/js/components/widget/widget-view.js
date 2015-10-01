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
		, menuItemExpandLabel: 'Menu item: Expand widget'
		, menuItemCollapseLabel: 'Menu item: Collapse widget'
		, widgetMenuHint: 'Widget menu'
	} );

	return Backbone.View.extend( {

		progressIcon: 'fa-spinner fa-spin',
		windowBodyContainerSelector: '.js-widget-body',

		builtinEvents: {
			'click .js-menu-refresh': '_onMenuRefreshClick'
		},

		constructor: function ( options ) {

			this.events = _.extend( this.builtinEvents, this.events );

			this.widgetTemplate = template;

			this.on( 'view:render', this.render, this );
			this.on( 'inner-view-rendered', this._onInnerViewRendered, this );

			Backbone.View.apply( this, [ options ] );
		},

		render: function() {

			this.$el.html( this.widgetTemplate( {
				icon: this.getIcon()
				, minHeight: this.widgetBodyMinHeight()
				, translator: translator
			} ) );

			this._showProgress();

			this.$( this.windowBodyContainerSelector ).fadeIn( 500, "swing" );

			this.renderBody();

			this.delegateEvents();
		},

		getTitle: function() {
			return '...';
		},

		getCupTitle: function( cup, title ) {
			return "<small><a href='/totalizator/categories/" + cup.category.categoryId + "/'>" +  cup.category.categoryName + "</a></small>"
					+ ":<small><strong>" + " <a class='text-info' href='/totalizator/cups/" + cup.cupId + "/'>" +  cup.cupName + "</a></strong></small> "
					+  ( title ? ' - ' + title : '' );
		},

		getTitleHint: function() {
			return ''; // NO HTML here
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

		widgetBodyMinHeight: function() {
			return '50px';
		},

		_showProgress: function() {
			var el = this._getIconEl();
			el.html( '' );
			el.removeClass( this.getIcon() );
			el.addClass( this.progressIcon );
		},

		_hideProgress: function() {

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

		footerHtml: function( text ) {
			this.$( '.js-footer' ).html( text );
		},

		setPanelClass: function( clazz ) {
			var panel = this.$( '.panel' );
			panel.removeClass( 'panel-default panel-info panel-success panel-warning panel-danger' );
			panel.addClass( clazz )
		},

		getDefaultWidgetTemplate: function() {
			return template;
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

			var menuEl = this.$( '.js-window-drop-down-menu' );

			var options = {
				menus: menuItems
				, menuButtonIcon: this.getIcon()
				, menuButtonText: ''
				, menuButtonHint: translator.widgetMenuHint
				, cssClass: ( menuEl.offset() && menuEl.offset().left > 400 ? 'dropdown-menu-right ' : '' ) + 'btn-default'
			};

			mainMenu( options, menuEl );
		},

		_onInnerViewRendered: function() {
			this._hideProgress();

			var title = this.$( '.js-widget-title' );

			if ( title.length > 1 ) {
				title = $( title[0] ); // hack for FearFox
			}

			title.html( this.getTitle() );
			title.attr( 'title', this.getTitleHint() );

			this._renderDropDownMenu();
		},

		_getIconEl: function() {
			return this.$( '.js-window-icon' );
		},

		_onMenuRefreshClick: function() {
			this.render();
		}
	});
} );
