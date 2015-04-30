define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/category-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Category overview"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.categoryId = options.options.categoryId;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			var category = this.model.toJSON();
			console.log( category );

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			return this;
		}
	} );
} );