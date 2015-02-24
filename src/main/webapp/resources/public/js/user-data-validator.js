define( function ( require ) {

	'use strict';

	var Translator = require( 'translator' );
	var translator = new Translator( {
		validationEmptyLoginLabel: 'Create user validation message: Enter login'
		, validationTooShortLoginLabel: 'Create user validation message: Login must have minimum 3 characters'
		, validationEmptyNameLabel: 'Create user validation message: Enter name'
		, validationTooShortNameLabel: 'Create user validation message: Name must have minimum 3 characters'
		, validationEmptyPasswordLabel: 'Create user validation message: Password can not be null'
		, validationConfirmPasswordLabel: 'Create user validation message: Confirm password'
		, validationPasswordsDoNotMatchLabel: 'Create user validation message: Entered passwords are not equal'
	} );

	return {

		validateLogin: function( login ) {
			var errors = [];

			if ( login == undefined || login == '' ) {
				errors.push( {
					message: translator.validationEmptyLoginLabel
				} );

				return errors;
			}

			if ( login.length < 3 ) {
				errors.push( {
					message: translator.validationTooShortLoginLabel
				} );
			}

			return errors;
		},

		validateName: function( name ) {
			var errors = [];

			if ( name == undefined || name == '' ) {
				errors.push( {
					message: translator.validationEmptyNameLabel
				} );

				return errors;
			}

			if ( name.length < 3 ) {
				errors.push( {
					message: translator.validationTooShortNameLabel
				} );
			}

			return errors;
		},

		_validatePassword: function( password ) {
			var errors = [];

			if ( password == undefined || password == '' ) {
				errors.push( {
					message: translator.validationEmptyPasswordLabel
				} );
			}

			return errors;
		},

		_validatePasswordConfirmation: function( password, password_confirmation ) {
			var errors = [];

			if ( password_confirmation == undefined || password_confirmation == '' ) {
				errors.push( {
					message: translator.validationConfirmPasswordLabel
				} );
			}

			if ( password != password_confirmation ) {
				errors.push( {
					message: translator.validationPasswordsDoNotMatchLabel
				} );
			}

			return errors;
		}
	}
});