define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var PageView = require( 'js/components/base-view/base-page-view' );

	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuPortalPageLabel: 'Portal page'
		, menuUsersLabel: 'Users'
		, menuAdminLabel: 'Administration'
		, menuControlPanelLabel: 'Admin: Control panel'
		, categoriesCupsTeamsLabel: 'Categories, cups, teams'
		, menuMatchesLabel: 'Matches'
		, pointsCalculationStrategiesLabel: 'Points calculation strategies'
		, gamesDataImportLabel: 'Games data import'
		, menuUntranslatedListLabel: 'Menu: Untranslated list'
		, menuReloadTranslationsLabel: 'Menu: Reload translations'
		, menuLogoutLabel: 'Menu: Logout'
		, dictionariesLabel: 'Admin: Main dictionaries'
		, jobsLabel: 'Jobs'
		, schedulerLabel: 'Scheduler'
	} );

	return PageView.extend( {

		builtinEvents: {
			'click .admin-reload-translations': '_reloadTranslations'
			, 'click .logout-link': 'logout'
		},

		mainMenuItems: function() {

			return [
				{ selector: '', icon: 'fa fa-home', link: '/betmen/', text: translator.menuPortalPageLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-cog', link: '/admin/', text: translator.menuAdminLabel }
				, { selector: '', icon: 'fa fa-cog', link: '/admin/control-panel/', text: translator.menuControlPanelLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-university', link: '/admin/categories-cups-teams/', text: translator.categoriesCupsTeamsLabel }
				, { selector: '', icon: 'fa fa-futbol-o', link: '/admin/matches/', text: translator.menuMatchesLabel }
				, { selector: '', icon: 'fa fa-balance-scale', link: '/admin/points-calculation-strategies/', text: translator.pointsCalculationStrategiesLabel }
				, { selector: '', icon: 'fa fa-map-o', link: '/admin/dictionaries/', text: translator.dictionariesLabel }
				, { selector: 'divider' }

				, { selector: '', icon: 'fa fa-exchange', link: '/admin/remote-games-import/', text: translator.gamesDataImportLabel }
				, { selector: 'divider' }

				, { selector: '', icon: 'fa fa-caret-square-o-right', link: '/admin/jobs/', text: translator.jobsLabel }
				, { selector: '', icon: 'fa fa-calendar-times-o', link: '/admin/scheduler/', text: translator.schedulerLabel }
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