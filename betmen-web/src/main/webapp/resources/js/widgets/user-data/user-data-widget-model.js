define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var dialog = require( 'js/dialog' );
	var validator = require( 'js/public/user-data-validator' );

	return Backbone.Model.extend( {

		_attrs: {},

		defaults: {
			userName: ''
		},

		initialize: function ( options ) {

			this.userId = options.options.userId;

			this.on( "invalid", function ( model, error ) {
				dialog.dialogValidationError( error );
			} );

			this.saveAttributes();
		},

		url: function() {
			return '/rest/users/settings/';
		},

		validate: function ( attrs, options ) {

			var errors = validator.validateName( this.get( 'userName' ) );

			if ( errors.length > 0 ) {
				return errors[0].message;
			}
		},

		saveAttributes: function() {
			this._attrs = this.toJSON();
		},

		restoreAttributes: function() {
			this.set( this._attrs );
		}
	});
} );

