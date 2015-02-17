define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!public/js/user/data/templates/user-data-template.html' );

	var LoginView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .save-button': '_onSaveButtonClick'
			, 'click .alert .close': '_onCloseErrorMessage'
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
			this._resetErrors();
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

		_validate: function() {
			var errors = [];

			var login = this.model.get( 'login' );
			if ( login == undefined || login == '' ) {
				errors.push( {
					message: 'Enter login'
					, control: this.$( '.form-group.login' )
					, clazz: 'has-error'
				} );
			}
			var name = this.model.get( 'name' );
			if ( name == undefined || name == '' ) {
				errors.push( {
					message: 'Enter name'
					, control: this.$( '.form-group.name' )
					, clazz: 'has-error'
				} );
			}

			var password = this.model.get( 'password' );
			if ( password == undefined || password == '' ) {
				errors.push( {
					message: 'Password can not be null'
					, control: this.$( '.form-group.password' )
					, clazz: 'has-error'
				} );
			}

			var password_confirmation = this.model.get( 'password_confirmation' );
			if ( password != password_confirmation ) {
				errors.push( {
					message: 'Entered passwords are not equal'
					, control: this.$( '.form-group.password_confirmation' )
					, clazz: 'has-error'
				} );
				errors.push( {
					message: ''
					, control: this.$( '.form-group.password_confirmation' )
					, clazz: 'has-error'
				} );
			}

			if ( errors.length > 0 ) {
				this._showError( errors );
			}
		},

		_showError: function( errors ) {

			var errorMessage = this.$( '.alert .error-message' );

			_.each( errors, function( error ) {
				if( error.message ) {
					errorMessage.append( '<div class="row">' + error.message + '</div>' );
					error.control.addClass( error.clazz );
				}
			});

			this.$( '.alert' ).show();
		},

		_onSaveButtonClick: function( evt ) {
			evt.preventDefault();

			this._save();
		},

		_resetErrors: function() {
			this.$( '.form-group.login' ).removeClass( 'has-error' );
			this.$( '.form-group.name' ).removeClass( 'has-error' );
			this.$( '.form-group.password' ).removeClass( 'has-error' );
			this.$( '.form-group.password_confirmation' ).removeClass( 'has-error' );

			this.$( '.alert .error-message' ).text( '' );

			this.$( '.alert' ).hide();
		},

		_onCloseErrorMessage: function( evt ) {
			evt.preventDefault();

			this._resetErrors();
		}
	} );

	return { LoginView: LoginView };
} );
