define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var PageView = require( 'js/components/base-view/base-page-view' );

	var adminService = require( '/resources/js/admin/services/admin-servise.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuPortalPageLabel: 'Portal page'
		, menuUsersLabel: 'Users'
		, menuAdminLabel: 'Administration'
		, menuMatchesLabel: 'Matches'
		, pointsCalculationStrategiesLabel: 'Points calculation strategies'
		, gamesDataImportLabel: 'Games data import'
		, menuUntranslatedListLabel: 'Menu: Untranslated list'
		, menuReloadTranslationsLabel: 'Menu: Reload translations'
		, menuLogoutLabel: 'Menu: Logout'
	} );

	return PageView.extend( {

		builtinEvents: {
			'click .admin-reload-translations': '_reloadTranslations'
			, 'click .logout-link': 'logout'
		},

		mainMenuItems: function() {

			return [
				{ selector: '', icon: 'fa fa-home', link: '/totalizator/', text: translator.menuPortalPageLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-cog', link: '/admin/', text: translator.menuAdminLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-futbol-o', link: '/admin/matches/', text: translator.menuMatchesLabel }
				, { selector: '', icon: 'fa fa-futbol-o', link: '/admin/points-calculation-strategies/', text: translator.pointsCalculationStrategiesLabel }
				, { selector: 'divider' }

				, { selector: '', icon: 'fa fa-exchange', link: '/admin/games-data-import/', text: translator.gamesDataImportLabel }
				, { selector: 'divider' }

				, { selector: '', icon: 'fa fa-language', link: '/admin/translations/', text: translator.menuUntranslatedListLabel }
				, { selector: 'admin-reload-translations', icon: 'fa fa-refresh', link: '#', text: translator.menuReloadTranslationsLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		},

		_reloadTranslations: function() {
			adminService.reloadTranslations();
			window.location.reload();
		}
	});
} );