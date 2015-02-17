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
			this._validate();
//			this.model.save();
		},

		_bindData: function() {
			this.model.set( {
				name: this.$( '#login' ).val()
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
					, control: this.$( '#login' )
					} );
			}

			if ( errors.length > 0 ) {
				this._showError( errors );
			}
		},

		_showError: function( errors ) {

			var errorMessage = this.$( '.alert .error-message' );

			_.each( errors, function( error ) {
				errorMessage.append( '<div class="row">' + error.message + '</div>' );
				error.control.addClass( 'alert-danger' );
			});

			this.$( '.alert' ).show();
		},

		_onSaveButtonClick: function( evt ) {
			evt.preventDefault();

			this._save();
		},

		_onCloseErrorMessage: function( evt ) {
			evt.preventDefault();

			this.$( '#login' ).removeClass( 'alert-danger' );
			this.$( '#password' ).removeClass( 'alert-danger' );
			this.$( '#password_confirmation' ).removeClass( 'alert-danger' );

			this.$( '.alert' ).hide();
		}
	} );

	return { LoginView: LoginView };
} );
