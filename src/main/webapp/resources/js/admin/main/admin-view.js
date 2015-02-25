define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/main/templates/admin-template.html' );

	var Services = require( '/resources/js/services.js' );

	var PageView = require( 'js/PageView' );

	var CategoriesModel = require( '/resources/js/admin/category/category-model.js' );
	var CategoriesView = require( '/resources/js/admin/category/category-view.js' );

	var CupsModel = require( '/resources/js/admin/cup/cup-model.js' );
	var CupsView = require( '/resources/js/admin/cup/cup-view.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: 'Admin: Page Title'
		, menuPortalPageLabel: 'Menu: Portal page'
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

		renderPage: function () {

			this.$el.html( this.template( {
				user: this.model.get( 'userDTO' )
				, translator: translator
			 } ) );

			this._renderCategories();

			this._renderCups();

			return this.$el;
		},

		mainMenuItems: function() {
			return [
				{ selector: '', icon: 'fa fa-home', link: '/totalizator/', text: translator.menuPortalPageLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-language', link: '/translations/', text: translator.menuUntranslatedListLabel }
				, { selector: 'admin-reload-translations', icon: 'fa fa-refresh', link: '#', text: translator.menuReloadTranslationsLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		},

		_renderCategories: function() {
			this.categoriesModel = new CategoriesModel.CategoriesModel();
			this.categoriesView = new CategoriesView.CategoriesView( { model: this.categoriesModel, el: this.$( '.admin-page-categories-container' ) } );

			this.categoriesView.on( 'events:categories_changed', this._updateCategories, this );
		},

		_renderCups: function() {
			this.cupsModel = new CupsModel.CupsModel();
			this.cupsView = new CupsView.CupsView( { model: this.cupsModel, el: this.$( '.admin-page-cup-container' ) } );
		},

		_updateCategories: function() {
			this.cupsView.trigger( 'events:categories_changed' );
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

