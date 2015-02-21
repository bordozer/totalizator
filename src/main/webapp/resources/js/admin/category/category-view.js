define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var TemplateEntry = require( 'text!js/admin/category/templates/category-template.html' );
	var TemplateList = require( 'text!js/admin/category/templates/categories-template.html' );

	var CategoryView = Backbone.View.extend( {

		templateEntry:_.template( TemplateEntry ),
		templateList:_.template( TemplateList ),

		events: {
//			'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );

			this.render();

//			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.templateList( {
				user: this.model.get( 'userDTO' )
			 } ) );

			return this.$el;
		}
	} );

	return { CategoryView: CategoryView };
} );

