define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Template = require( 'text!public/js/login/templates/login-view-template.html' );

	var LoginCompositeView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
		},

		render: function () {

			this.$el.html( this.template( {

			 } ) );

			return this.$el;
		}
	} );

	return { LoginCompositeView: LoginCompositeView };
} );

