define( [ 'jquery' ], function ( $ ) {

	return {

		validateLogin: function( login ) {
			var errors = [];

			if ( login == undefined || login == '' ) {
				errors.push( {
					message: 'Enter login'
				} );

				return errors;
			}

			if ( login.length < 3 ) {
				errors.push( {
					message: 'Login must have minimum 3 characters'
				} );
			}

			return errors;
		},

		validateName: function( name ) {
			var errors = [];

			if ( name == undefined || name == '' ) {
				errors.push( {
					message: 'Enter name'
				} );

				return errors;
			}

			if ( name.length < 3 ) {
				errors.push( {
					message: 'Name must have minimum 3 characters'
				} );
			}

			return errors;
		},

		_validatePassword: function( password ) {
			var errors = [];

			if ( password == undefined || password == '' ) {
				errors.push( {
					message: 'Password can not be null'
				} );
			}

			return errors;
		},

		_validatePasswordConfirmation: function( password, password_confirmation ) {
			var errors = [];

			if ( password_confirmation == undefined || password_confirmation == '' ) {
				errors.push( {
					message: 'Confirm password'
				} );
			}

			if ( password != password_confirmation ) {
				errors.push( {
					message: 'Entered passwords are not equal'
				} );
			}

			return errors;
		}
	}
});