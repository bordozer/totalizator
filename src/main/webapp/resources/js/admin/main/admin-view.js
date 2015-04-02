define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/main/templates/admin-template.html' );

	var Services = require( '/resources/js/services/service.js' );

	var CategoriesModel = require( 'js/admin/category/category-model' );
	var CategoriesView = require( 'js/admin/category/category-view' );

	var CupsModel = require( 'js/admin/cup/cup-model' );
	var CupsView = require( 'js/admin/cup/cup-view' );

	var TeamsModel = require( 'js/admin/team/team-model' );
	var TeamsView = require( 'js/admin/team/team-view' );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {

			this.categoriesModel = new CategoriesModel.CategoriesModel();
			this.cupsModel = new CupsModel.CupsModel();
			this.teamsModel = new TeamsModel.TeamsModel();

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

