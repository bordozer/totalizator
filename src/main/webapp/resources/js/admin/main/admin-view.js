define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/main/templates/admin-template.html' );

	var Services = require( '/resources/js/services.js' );

	var CategoriesModel = require( '/resources/js/admin/category/category-model.js' );
	var CategoriesView = require( '/resources/js/admin/category/category-view.js' );

	var CupsModel = require( '/resources/js/admin/cup/cup-model.js' );
	var CupsView = require( '/resources/js/admin/cup/cup-view.js' );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				user: this.model.get( 'userDTO' )
			 } ) );

			this._renderCategories();

			this._renderCups();

			return this.$el;
		},

		_renderCategories: function() {
			var model = new CategoriesModel.CategoriesModel();
			var view = new CategoriesView.CategoriesView( { model: model, el: this.$( '.admin-page-categories-container' ) } );
		},

		_renderCups: function() {
			var model = new CupsModel.CupsModel();
			var view = new CupsView.CupsView( { model: model, el: this.$( '.admin-page-cup-container' ) } );
		},

		_logout: function() {
//			if ( confirm( 'Logout?' ) ) {
				Services.logout();
//			}
		}

	} );

	return { AdminView: AdminView };
} );

