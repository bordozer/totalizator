define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Template = require( 'text!public/js/login-form/templates/login-form-template.html' );

	var LoginFormView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .create-new-user-button': '_onCreateNewUserButtonClick'
		},

		initialize: function() {
			this.model.on( 'sync', this.render, this );
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
			 } ) );

			return this.$el;
		},

		_onCreateNewUserButtonClick: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:create_new_user_clicked' );
		}
	} );

	return { LoginFormView: LoginFormView };
} );
