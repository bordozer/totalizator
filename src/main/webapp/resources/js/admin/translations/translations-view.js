define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/translations/templates/translations-template.html' );

	var Services = require( '/resources/js/services.js' );

	var PageView = require( 'js/base/base-page-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: 'Translations: Page Title'
		, menuPortalPageLabel: 'Menu: Portal page'
		, menuMatchesLabel: 'Menu: Matches'
		, menuAdminLabel: 'Menu: Admin'
		, menuReloadTranslationsLabel: 'Menu: Reload translations'
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var AdminView = PageView.extend( {

		template:_.template( Template ),

		events: {
			'click .admin-reload-translations': '_reloadTranslations'
			, 'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		getPageSubTitle: function() {
			return translator.pageTitle;
		},

		renderBody: function ( el ) {

			el.html( this.template( {
				userName: this.model.get( 'userName' )
				, untranslatedList: this.model.get( 'untranslatedList' )
				, translator: translator
			 } ) );

			return this.$el;
		},

		mainMenuItems: function() {
			return [
				{ selector: '', icon: 'fa fa-home', link: '/totalizator/', text: translator.menuPortalPageLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-futbol-o', link: '/admin/matches/', text: translator.menuMatchesLabel }
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

