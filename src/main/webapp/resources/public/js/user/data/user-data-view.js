define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Validator = require( 'js/services/user-data-validator' );

	var Template = require( 'text!public/js/user/data/templates/user-data-template.html' );

	var LoginView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .save-button': '_onSaveButtonClick'
			, 'click .alert .close': '_onCloseErrorMessage'
			, 'focusout .form-group.login': '_onLeaveLogin'
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
//			this._resetErrors();
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

		_validate: function() {
			this._validateLogin();
			/*var errors = Validator.validate( this.model );

			if ( errors.length > 0 ) {
				this._addErrors( errors );
			}*/
		},

		_addErrors: function( control, errors, messageContainer ) {

			if ( errors.length == 0 ) {
				control.removeClass( 'has-error' );
				messageContainer.hide();
				control.addClass( 'has-success' );

				return;
			}

//			var messageContainer = this.$( '.alert .error-message' );
			var self = this;

			messageContainer.text( '' );
			_.each( errors, function( error ) {
				if( error.message ) {
					messageContainer.append( '<div class="row">' + error.message + '</div>' );
					control.addClass( 'has-error' );
				}
			});

//			if ( messageContainer.text() != '' ) {
				messageContainer.show();
//			}
		},

		/*_getControl: function( field ) {

			if ( field == 'login' ) {
				return this.$( '.form-group.login' );
			}

			if ( field == 'name' ) {
				return this.$( '.form-group.name' );
			}

			if ( field == 'password' ) {
				return this.$( '.form-group.password' );
			}

			if ( field == 'password_confirmation' ) {
				return this.$( '.form-group.password_confirmation' );
			}

			return null;
		},*/

		_onLeaveLogin: function( evt ) {
			evt.preventDefault();

			this._bindData();

			this._validateLogin();
		},

		_onSaveButtonClick: function( evt ) {
			evt.preventDefault();

			this._save();
		},

		/*_resetErrors: function() {
			this.$( '.form-group.login' ).removeClass( 'has-error' );
			this.$( '.form-group.name' ).removeClass( 'has-error' );
			this.$( '.form-group.password' ).removeClass( 'has-error' );
			this.$( '.form-group.password_confirmation' ).removeClass( 'has-error' );

			this.$( '.alert .error-message' ).text( '' );

			this.$( '.alert' ).hide();
		},*/

		_onCloseErrorMessage: function( evt ) {
			evt.preventDefault();

			this._resetErrors();
		}
	} );

	return { LoginView: LoginView };
} );
