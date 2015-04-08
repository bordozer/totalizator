define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Users"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.$el.html( template( {
				translator: translator
			} ) );
		}
	});
} );
