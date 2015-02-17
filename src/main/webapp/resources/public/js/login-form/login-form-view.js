define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Template = require( 'text!public/js/login-form/templates/login-form-template.html' );

	var LoginFormView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function() {
			this.model.on( 'sync', this.render, this );
//			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
			 } ) );

			return this.$el;
		}
	} );

	return { LoginFormView: LoginFormView };
} );
