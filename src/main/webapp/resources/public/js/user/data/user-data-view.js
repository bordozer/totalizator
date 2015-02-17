define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Validator = require( 'js/services/user-data-validator' );

	var Template = require( 'text!public/js/user/data/templates/user-data-template.html' );

	var UserDataView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .save-button': '_onSaveButtonClick'
			, 'click .alert .close': '_onCloseErrorMessage'
			, 'focusout .form-group.login': '_onLeaveLogin'
			, 'focusout .form-group.name': '_onLeaveName'
			, 'focusout .form-group.password, .form-group.password_confirmation': '_onLeavePassword'
		},

		initialize: function() {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
				, title: this.model.get( 'id' ) > 0 ? 'Edit data' : 'New user'
			 } ) );

			return this.$el;
		},

		_save: function() {
			this._bindData();
			this._validate();
//			this.model.save();
		},

		_bindData: function() {
			this.model.set( {
				login: this.$( '#login' ).val()
				, name: this.$( '#name' ).val()
				, password: this.$( '#password' ).val()
				, password_confirmation: this.$( '#password_confirmation' ).val()
			} );
		},

		_validateLogin: function() {
			var errors = Validator.validateLogin( this.model );

			var control = this.$( '.form-group.login' );
			var errorContainer = this.$( '.login-errors' );

			this._addErrors( control, errors, errorContainer );
		},


		_validateName: function() {
			var errors = Validator.validateName( this.model );

			var control = this.$( '.form-group.name' );
			var errorContainer = this.$( '.name-errors' );

			this._addErrors( control, errors, errorContainer );
		},


		validatePassword: function() {
			var errors = Validator.validatePassword( this.model );

			var control = this.$( '.form-group.password' );
			var errorContainer = this.$( '.password-errors' );

			this._addErrors( control, errors, errorContainer );
		},


		validatePasswordConfirmation: function() {
			var errors = Validator.validatePasswordConfirmation( this.model );

			var control = this.$( '.form-group.password_confirmation' );
			var errorContainer = this.$( '.password_confirmation-errors' );

			this._addErrors( control, errors, errorContainer );
		},

		_validate: function() {
			this._validateLogin();
		},

		_addErrors: function( control, errors, messageContainer ) {

			if ( errors.length == 0 ) {
				control.removeClass( 'has-error' );
				messageContainer.hide();
				control.addClass( 'has-success' );

				return;
			}

			messageContainer.text( '' );
			_.each( errors, function( error ) {
				messageContainer.append( '<li>' + error.message + '</li>' );
				control.addClass( 'has-error' );
			});

			messageContainer.show();
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

			this.validatePassword();
			this.validatePasswordConfirmation();
		},

		_onSaveButtonClick: function( evt ) {
			evt.preventDefault();

			this._save();
		},

		_onCloseErrorMessage: function( evt ) {
			evt.preventDefault();

			this._resetErrors();
		}
	} );

	return { UserDataView: UserDataView };
} );
