define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var PageView = require( 'js/base/base-page-view' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuPortalPageLabel: 'Menu: Portal page'
		, menuAdminLabel: 'Menu: Admin'
		, menuMatchesLabel: 'Menu: Matches'
		, menuUntranslatedListLabel: 'Menu: Untranslated list'
		, menuReloadTranslationsLabel: 'Menu: Reload translations'
		, menuLogoutLabel: 'Menu: Logout'
	} );

	return PageView.extend( {

		builtinEvents: {
			'click .admin-reload-translations': '_reloadTranslations'
			, 'click .logout-link': 'logout'
		},

		initialize: function( options ) {

		},

		mainMenuItems: function() {
			return [
				{ selector: '', icon: 'fa fa-home', link: '/totalizator/', text: translator.menuPortalPageLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-cog', link: '/admin/', text: translator.menuAdminLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-futbol-o', link: '/admin/matches/', text: translator.menuMatchesLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-language', link: '/admin/translations/', text: translator.menuUntranslatedListLabel }
				, { selector: 'admin-reload-translations', icon: 'fa fa-refresh', link: '#', text: translator.menuReloadTranslationsLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		},

		_reloadTranslations: function() {
			Services.reloadTranslations();
		}
	});
} );