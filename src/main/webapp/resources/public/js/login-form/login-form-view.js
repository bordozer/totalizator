define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Validator = require( 'js/services/user-data-validator' );
	var FormValidation = require( 'public/js/validationMessenger' );

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
			this._validateLogin();
			this._validatePassword();
		},

		_validateLogin: function() {
			var login = this.$( '#login' ).val();
			var loginErrors = Validator.validateLogin( this.model.get( 'login' ) );
			var loginControl = this.$( '.form-group.login' );
			var loginErrorContainer = this.$( '.login-errors' );
			FormValidation.addErrors( loginControl, loginErrors, loginErrorContainer );
		},

		_validatePassword: function() {
			var password = this.$( '#password' ).val();
			var passwordErrors = Validator._validatePassword( this.model.get( 'password' ) );
			var passwordControl = this.$( '.form-group.password' );
			var passwordErrorContainer = this.$( '.password-errors' );
			FormValidation.addErrors( passwordControl, passwordErrors, passwordErrorContainer );
		},

		_onLoginButtonClick: function( evt ) {
			evt.preventDefault();

			this._login();
		}
	} );

	return { LoginFormView: LoginFormView };
} );
