define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Template = require( 'text!public/js/login-form/templates/login-form-template.html' );

	var LoginFormView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .create-new-user-button': '_onCreateNewUserButtonClick'
			, 'click .login-button': '_onLoginButtonClick'
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
		},

		_login: function() {
			this._validate();
		},

		_validate: function() {
			var login = this.$( '#login' ).val();
			var password = this.$( '#password' ).val();

			console.log( login, password);
		},

		_onLoginButtonClick: function( evt ) {
			evt.preventDefault();

			this._login();
		}
	} );

	return { LoginFormView: LoginFormView };
} );
