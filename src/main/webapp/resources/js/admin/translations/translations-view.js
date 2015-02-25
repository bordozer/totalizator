define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/translations/templates/translations-template.html' );

	var Services = require( '/resources/js/services.js' );

	var mainMenu = require( 'js/main-menu/main-menu' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: 'Translations: Page Title'
		, menuPortalPageLabel: 'Menu: Back to site'
		, menuAdminLabel: 'Menu: Admin'
		, menuReloadTranslationsLabel: 'Menu: Reload translations'
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .admin-reload-translations': '_reloadTranslations'
			, 'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				userName: this.model.get( 'userName' )
				, untranslatedList: this.model.get( 'untranslatedList' )
				, translator: translator
			 } ) );

			this.renderMenu();

			return this.$el;
		},

		renderMenu: function() {
			mainMenu( this.menus(), this.$( '.main-menu-container') );
		},

		menus: function() {
			return [
				{ selector: '', icon: 'fa fa-backward', link: '/totalizator/', text: translator.menuPortalPageLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-cog', link: '/admin/', text: translator.menuAdminLabel }
				, { selector: 'divider' }
				, { selector: 'admin-reload-translations', icon: 'fa fa-refresh', link: '#', text: translator.menuReloadTranslationsLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		},

		_reloadTranslations: function() {
			Services.reloadTranslations();
		},

		_logout: function() {
			Services.logout();
		}

	} );

	return { AdminView: AdminView };
} );

