define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateEntry = require( 'text!js/admin/category/templates/category-template.html' );
	var TemplateList = require( 'text!js/admin/category/templates/categories-template.html' );

	var CategoriesView = Backbone.View.extend( {


		template: _.template( TemplateList ),

		events: {
//			'click .logout-link': '_logout'
		},

		initialize: function ( options ) {

			this.model.on( 'add', this.renderEntry, this );

			this.render();

			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
			} ) );

			return this.$el;
		},

		renderEntry: function ( model ) {
			var view = new CategoryView( {
				model: model
			} );

			return this.$( '.categories-container' ).append( view.render().$el );
		}
	} );

	var CategoryView = Backbone.View.extend( {

		template: _.template( TemplateEntry ),

		initialize: function ( options ) {
		},

		render: function () {
			var modelJSON = this.model.toJSON();

			this.$el.html( this.template( {
				model: modelJSON
			} ) );

			return this;
		}
	} );

	return { CategoriesView: CategoriesView };
} );

