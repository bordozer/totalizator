define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!./templates/admin-template.html' );

	var CategoriesModel = require( 'js/admin/widgets/categories/admin-categories-model' );
	var CategoriesView = require( 'js/admin/widgets/categories/admin-categories-view' );

	var CupsModel = require( 'js/admin/widgets/cups/admin-cups-model' );
	var CupsView = require( 'js/admin/widgets/cups/admin-cups-view' );

	var TeamsModel = require( 'js/admin/widgets/teams/admin-teams-model' );
	var TeamsView = require( 'js/admin/widgets/teams/admin-teams-view' );

	var service = require( '/resources/js/services/service.js' );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {

			this.categoriesModel = new CategoriesModel.CategoriesModel();
			this.cupsModel = new CupsModel.CupsModel();
			this.teamsModel = new TeamsModel.TeamsModel();

			var categories = service.loadCategories();
			if ( categories.length > 0  ) {
				var preselectedCategory = categories[0];
				this.categoriesModel.selectedCategoryId = preselectedCategory.categoryId;
				this.cupsModel.filterByCategory = preselectedCategory.categoryId;
				this.teamsModel.filterByCategory = preselectedCategory.categoryId;
			}

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				user: this.model.get( 'userDTO' )
			 } ) );

			this._renderCategories();
			this._renderCups();
			this._renderTeams();

			return this.$el;
		},

		_getPreselectedCategory: function() {

		},

		_renderCategories: function() {
			this.categoriesView = new CategoriesView.CategoriesView( { model: this.categoriesModel, el: this.$( '.admin-page-categories-container' ) } );

			this.categoriesView.on( 'events:categories_changed', this._updateCategories, this );
			this.categoriesView.on( 'events:filter_by_category', this._filterByCategory, this );
		},

		_renderCups: function() {
			this.cupsView = new CupsView.CupsView( { model: this.cupsModel, el: this.$( '.admin-page-cups-container' ) } );
		},

		_renderTeams: function() {
			this.teamsView = new TeamsView.TeamsView( { model: this.teamsModel, el: this.$( '.admin-page-teams-container' ) } );
		},

		_updateCategories: function() {
			this.cupsView.trigger( 'events:categories_changed' );
			this.teamsView.trigger( 'events:categories_changed' );
		},

		_filterByCategory: function( options ) {
			this.cupsView.trigger( 'events:filter_by_category', options );
			this.teamsView.trigger( 'events:filter_by_category', options );
		}

	} );

	return { AdminView: AdminView };
} );

