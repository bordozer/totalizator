define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/categories-cups-teams-template.html' ) );

	var CategoriesModel = require( 'js/admin/widgets/categories/admin-categories-model' );
	var CategoriesView = require( 'js/admin/widgets/categories/admin-categories-view' );

	var CupsModel = require( 'js/admin/widgets/cups/admin-cups-model' );
	var CupsView = require( 'js/admin/widgets/cups/admin-cups-view' );

	var TeamsModel = require( 'js/admin/widgets/teams/admin-teams-model' );
	var TeamsView = require( 'js/admin/widgets/teams/admin-teams-view' );

	var service = require( '/resources/js/services/service.js' );
	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function( options ) {

			this.categoriesModel = new CategoriesModel.CategoriesModel();
			this.cupsModel = new CupsModel.CupsModel();
			this.teamsModel = new TeamsModel();

			this._preselectCategory();

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				user: this.model.get( 'userDTO' )
			 } ) );

			this._renderCategories();
			this._renderCups();
			this._renderTeams();

			return this.$el;
		},

		_getPreselectedCategory: function() {
			var categories = service.loadCategories();
			return categories.length > 0 ? categories[ 0 ] : null ;
		},

		_preselectCategory: function() {
			var preselectedCategory = this._getPreselectedCategory();
			if ( preselectedCategory  ) {
				this.categoriesModel.selectedCategoryId = preselectedCategory.categoryId;
				this.cupsModel.filterByCategory = preselectedCategory.categoryId;
				this.teamsModel.filterByCategory = preselectedCategory.categoryId;
			}

			return preselectedCategory;
		},

		_getPreselectedCup: function( preselectedCategory ) {
			var cups = adminService.loadCups();
			var cupsByCategory = service.filterCupsByCategory( cups, preselectedCategory.categoryId );
			return cupsByCategory.length > 0 ? cupsByCategory[ 0 ] : null ;
		},

		_preselectCup: function( preselectedCategory ) {
			var preselectedCup = this._getPreselectedCup( preselectedCategory );
			if ( preselectedCup  ) {
				this.cupsModel.selectedCup = preselectedCup;
				this.teamsModel.selectedCup = preselectedCup;
			}
			return preselectedCup;
		},

		_renderCategories: function() {
			this.categoriesView = new CategoriesView.CategoriesView( { model: this.categoriesModel, el: this.$( '.admin-page-categories-container' ) } );

			this.categoriesView.on( 'events:categories_changed', this._updateCategories, this );
			this.categoriesView.on( 'events:filter_by_category', this._filterByCategory, this );
		},

		_renderCups: function() {
			this.cupsView = new CupsView.CupsView( { model: this.cupsModel, el: this.$( '.admin-page-cups-container' ) } );
			this.cupsView.on( 'events:admin:cup:selected', this._filterTeamsByCup, this );
		},

		_renderTeams: function() {
			this.teamsView = new TeamsView( { model: this.teamsModel, el: this.$( '.admin-page-teams-container' ) } );
		},

		_updateCategories: function() {
			this.cupsView.trigger( 'events:categories_changed' );
			this.teamsView.trigger( 'events:categories_changed' );
		},

		_filterByCategory: function( options ) {
			this.cupsView.trigger( 'events:filter_by_category', options );
			this.teamsView.trigger( 'events:filter_by_category', options );
		},

		_filterTeamsByCup: function( options ) {
			this.teamsView.trigger( 'events:admin:cup:selected', options );
		}
	} );
} );