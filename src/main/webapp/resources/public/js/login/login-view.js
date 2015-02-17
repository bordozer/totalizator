define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Template = require( 'text!public/js/login/templates/login-template.html' );

	var LoginView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {

			 } ) );

			return this.$el;
		}
	} );

	return { LoginView: LoginView };
} );
