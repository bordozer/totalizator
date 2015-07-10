define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var Validator = require( 'public/js/user-data-validator' );
	var FormValidation = require( 'public/js/validationMessenger' );

	var Template = require( 'text!public/js/login-form/templates/login-form-template.html' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		loginLabel: 'Login page: Login'
		, passwordLabel: 'Login page: Password'
		, createNewUserLabel: 'Login page: Create new user'
		, rememberMeLabel: 'Login page: Remember me'
	} );

	var LoginFormView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .create-new-user-button': '_onCreateNewUserButtonClick'
			, 'click .login-button': '_onLoginButtonClick'
		},

		initialize: function() {
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
				, translator: translator
			 } ) );

			var self = this;
			this.$( 'input' ).keypress( function ( e ) {
				if ( e.which == 13 ) {
					self._processLogin();
				}
			} );

			return this.$el;
		},

		_authenticate: function() {
			var data = { login: this.model.get( 'login' ), password: this.model.get( 'password' ), language: this.model.language };
			this.trigger( 'events:authenticate', data );
		},

		_onCreateNewUserButtonClick: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:create_new_user_clicked' );
		},

		_processLogin: function() {
			this._bindData();
			if( this._validate() ) {
				this._authenticate();
			}
		},

		_validate: function() {
			return this._validateLogin() && this._validatePassword();
		},

		_bindData: function() {
			this.model.set( { login: this.$( '#login' ).val(), password: this.$( '#password' ).val() } );
			this.model.language = this.$( 'input[name=language]:checked' ).val();
		},

		_validateLogin: function() {
			var login = this.model.get( 'login' );
			var loginErrors = Validator.validateLogin( login );
			var loginControl = this.$( '.form-group.login' );
			var loginErrorContainer = this.$( '.login-errors' );
			FormValidation.addErrors( loginControl, loginErrors, loginErrorContainer );

			return loginErrors.length == 0;
		},

		_validatePassword: function() {
			var password = this.model.get( 'password' );
			var passwordErrors = Validator._validatePassword( password );
			var passwordControl = this.$( '.form-group.password' );
			var passwordErrorContainer = this.$( '.password-errors' );
			FormValidation.addErrors( passwordControl, passwordErrors, passwordErrorContainer );

			return passwordErrors.length == 0;
		},

		_onLoginButtonClick: function( evt ) {
			evt.preventDefault();

			this._processLogin();
		}
	} );

	return { LoginFormView: LoginFormView };
} );
