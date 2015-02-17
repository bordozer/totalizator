define( [ 'jquery' ], function ( $ ) {

	return {

		/*validate: function( model ) {
			var errors = [];

			var loginErrors = this.validateLogin( model );
			var nameErrors = this.validateName( model );
			var passwordErrors = this.validatePassword( model );
			var passwordConfirmationErrors = this.validatePasswordConfirmation( model );

			return _.union( loginErrors, nameErrors, passwordErrors, passwordConfirmationErrors );
		},*/

		validateLogin: function( model ) {
			var errors = [];

			var login = model.get( 'login' );
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

		validateName: function( model ) {
			var errors = [];

			var name = model.get( 'name' );
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

		validatePassword: function( model ) {
			var errors = [];

			var password = model.get( 'password' );
			if ( password == undefined || password == '' ) {
				errors.push( {
					message: 'Password can not be null'
				} );
			}

			return errors;
		},

		validatePasswordConfirmation: function( model ) {
			var errors = [];

			var password = model.get( 'password' );
			var password_confirmation = model.get( 'password_confirmation' );

			if ( password != password_confirmation ) {
				errors.push( {
					message: 'Entered passwords are not equal'
				} );
			}

			return errors;
		}
	}
});