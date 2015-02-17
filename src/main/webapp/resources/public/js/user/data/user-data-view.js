define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Validator = require( 'public/js/user-data-validator' );
	var FormValidation = require( 'public/js/validationMessenger' );

	var Template = require( 'text!public/js/user/data/templates/user-data-template.html' );

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
			this.model.on( 'sync', this.render, this );
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
				, isNewUserRegistration: this.model.get( 'id' ) == 0
				, title: this.model.get( 'id' ) > 0 ? 'Edit data' : 'New user'
			 } ) );

			return this.$el;
		},

		_save: function() {
			this._bindData();
			this._validate();
			this.model.save();

			/*var data = { login: this.model.get( 'login' ), name: this.model.get( 'name' ), password: this.model.get( 'password' ) };
			$.ajax( {
				method: 'POST',
				url: '/user/create/',
				data: data,
				headers: {
					"Content-Type": "application/json",
					"Accept": "text/plain"
				},
				success: function ( response ) {
					console.log( response );
				}
			} );*/
		},

		_bindData: function() {
			this.model.set( {
				login: this.$( '#login' ).val()
				, name: this.$( '#name' ).val()
				, password: this.$( '#password' ).val()
			} );
			this.model.password_confirmation = this.$( '#password_confirmation' ).val();
		},

		_validateLogin: function() {
			var errors = Validator.validateLogin( this.model.get( 'login' ) );

			var control = this.$( '.form-group.login' );
			var errorContainer = this.$( '.login-errors' );

			FormValidation.addErrors( control, errors, errorContainer );
		},


		_validateName: function() {
			var errors = Validator.validateName( this.model.get( 'name' ) );

			var control = this.$( '.form-group.name' );
			var errorContainer = this.$( '.name-errors' );

			FormValidation.addErrors( control, errors, errorContainer );
		},


		_validatePassword: function() {
			var errors = Validator._validatePassword( this.model.get( 'password' ) );

			var control = this.$( '.form-group.password' );
			var errorContainer = this.$( '.password-errors' );

			FormValidation.addErrors( control, errors, errorContainer );
		},

		_validatePasswordConfirmation: function() {
			var errors = Validator._validatePasswordConfirmation( this.model.get( 'password' ), this.model.password_confirmation );

			var control = this.$( '.form-group.password_confirmation' );
			var errorContainer = this.$( '.password_confirmation-errors' );

			FormValidation.addErrors( control, errors, errorContainer );
		},

		_validate: function() {
			this._validateLogin();
			this._validateName();
			this._validatePassword();
			this._validatePasswordConfirmation();
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
