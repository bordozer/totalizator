define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!public/js/header/templates/header-template.html' );

	var HeaderView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.render( options.specialContainer );
		},

		render: function ( specialContainer ) {

			this.$el.html( this.template( {
				model: this.model
			 } ) );

			this.$( '.header-specific' ).html( specialContainer );
			specialContainer.show();

			return this.$el;
		}
	} );

	return { HeaderView: HeaderView };
} );

