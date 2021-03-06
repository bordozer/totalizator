define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Validator = require( 'js/public/user-data-validator' );
	var FormValidation = require( 'js/public/validationMessenger' );

	var Template = require( 'text!js/public/user/data/templates/user-data-template.html' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		newUserDataTitleLabel: 'Create user'
		, userDataLoginLabel: 'Create login'
		, userDataNameLabel: 'User name'
		, userDataPasswordLabel: 'Create password'
		, userDataConfirmPasswordLabel: 'Create password confirmation'
		, userDataBackToLoginPageLabel: 'Back to login'
		, userDataSaveButtonLabel: 'Register user'
	} );

	var UserDataView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .save-button': '_onSaveButtonClick'
			, 'click .back-to-login-button': '_onBackToLoginClick'
			, 'focusout .form-group.login': '_onLeaveLogin'
			, 'focusout .form-group.name': '_onLeaveName'
			, 'focusout .form-group.password, .form-group.password_confirmation': '_onLeavePassword'
		},

		initialize: function() {
			this.model.on( 'sync', this._authenticate, this );
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
				, translator: translator
			 } ) );

			return this.$el;
		},

		_save: function() {
			this._bindData();
			if ( this._validate() ) {
				this.model.save();
			}
		},

		_authenticate: function() {
			var data = { login: this.model.get( 'login' ), password: this.model.get( 'password' ), language: this._getLanguage() };
			this.trigger( 'events:authenticate', data );
		},

		_bindData: function() {
			this.model.set( {
				login: this.$( '#login' ).val()
				, name: this.$( '#name' ).val()
				, password: this.$( '#password' ).val()
				, language: this._getLanguage()
			} );
			this.model.password_confirmation = this.$( '#password_confirmation' ).val();
		},

		_getLanguage: function() {
			return this.$( "input[name='language']:checked" ).val();
		},

		_validate: function() {
			var isValid = this._validateLogin();
			isValid = this._validateName() && isValid;
			isValid = this._validatePassword() && isValid;
			isValid = this._validatePasswordConfirmation() && isValid;

			return isValid;
		},

		_validateLogin: function() {
			var errors = Validator.validateLogin( this.model.get( 'login' ) );

			var control = this.$( '.form-group.login' );
			var errorContainer = this.$( '.login-errors' );

			FormValidation.addErrors( control, errors, errorContainer );

			return errors.length == 0
		},


		_validateName: function() {
			var errors = Validator.validateName( this.model.get( 'name' ) );

			var control = this.$( '.form-group.name' );
			var errorContainer = this.$( '.name-errors' );

			FormValidation.addErrors( control, errors, errorContainer );

			return errors.length == 0
		},


		_validatePassword: function() {
			var errors = Validator._validatePassword( this.model.get( 'password' ) );

			var control = this.$( '.form-group.password' );
			var errorContainer = this.$( '.password-errors' );

			FormValidation.addErrors( control, errors, errorContainer );

			return errors.length == 0
		},

		_validatePasswordConfirmation: function() {
			var errors = Validator._validatePasswordConfirmation( this.model.get( 'password' ), this.model.password_confirmation );

			var control = this.$( '.form-group.password_confirmation' );
			var errorContainer = this.$( '.password_confirmation-errors' );

			FormValidation.addErrors( control, errors, errorContainer );

			return errors.length == 0
		},

		_onLeaveLogin: function( evt ) {
			evt.preventDefault();

			this._bindData();

			this._validateLogin();
		},

		_onLeaveName: function( evt ) {
			evt.preventDefault();

			this._bindData();

			this._validateName();
		},

		_onLeavePassword: function( evt ) {
			evt.preventDefault();

			this._bindData();

			this._validatePassword();
			this._validatePasswordConfirmation();
		},

		_onSaveButtonClick: function( evt ) {
			evt.preventDefault();
			this._save();
		},

		_onBackToLoginClick: function( evt ) {
			evt.preventDefault();

			this.trigger( 'events:back_to_login' );
		}
	} );

	return { UserDataView: UserDataView };
} );
