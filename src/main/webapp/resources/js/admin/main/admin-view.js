define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/main/templates/admin-template.html' );

	var Services = require( '/resources/js/services.js' );

	var PageView = require( 'js/base/base-page-view' );

	var CategoriesModel = require( '/resources/js/admin/category/category-model.js' );
	var CategoriesView = require( '/resources/js/admin/category/category-view.js' );

	var CupsModel = require( '/resources/js/admin/cup/cup-model.js' );
	var CupsView = require( '/resources/js/admin/cup/cup-view.js' );

	var TeamsModel = require( '/resources/js/admin/team/team-model.js' );
	var TeamsView = require( '/resources/js/admin/team/team-view.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: 'Admin: Page Title'
		, menuPortalPageLabel: 'Menu: Portal page'
		, menuMatchesLabel: 'Menu: Matches'
		, menuUntranslatedListLabel: 'Menu: Untranslated list'
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
				user: this.model.get( 'userDTO' )
				, translator: translator
			 } ) );

			this._renderCategories();

			this._renderCups();

			this._renderTeams();

			return this.$el;
		},

		mainMenuItems: function() {
			return [
				{ selector: '', icon: 'fa fa-home', link: '/totalizator/', text: translator.menuPortalPageLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-futbol-o', link: '/admin/matches/', text: translator.menuMatchesLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-language', link: '/admin/translations/', text: translator.menuUntranslatedListLabel }
				, { selector: 'admin-reload-translations', icon: 'fa fa-refresh', link: '#', text: translator.menuReloadTranslationsLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		},

		_renderCategories: function() {
			this.categoriesModel = new CategoriesModel.CategoriesModel();
			this.categoriesView = new CategoriesView.CategoriesView( { model: this.categoriesModel, el: this.$( '.admin-page-categories-container' ) } );

			this.categoriesView.on( 'events:categories_changed', this._updateCategories, this );
			this.categoriesView.on( 'events:filter_by_category', this._filterByCategory, this );
		},

		_renderCups: function() {
			this.cupsModel = new CupsModel.CupsModel();
			this.cupsView = new CupsView.CupsView( { model: this.cupsModel, el: this.$( '.admin-page-cups-container' ) } );
		},

		_renderTeams: function() {
			this.teamsModel = new TeamsModel.TeamsModel();
			this.teamsView = new TeamsView.TeamsView( { model: this.teamsModel, el: this.$( '.admin-page-teams-container' ) } );
		},

		_updateCategories: function() {
			this.cupsView.trigger( 'events:categories_changed' );
			this.teamsView.trigger( 'events:categories_changed' );
		},

		_filterByCategory: function( options ) {
			this.cupsView.trigger( 'events:filter_by_category', options );
			this.teamsView.trigger( 'events:filter_by_category', options );
		},

		_logout: function() {
			Services.logout();
		}

	} );

	return { AdminView: AdminView };
} );

