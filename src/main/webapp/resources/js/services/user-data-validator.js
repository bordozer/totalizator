define( [ 'jquery' ], function ( $ ) {

	return {

		validate: function( model ) {
			var errors = [];

			var loginErrors = this.validateLogin( model );
			var nameErrors = this.validateName( model );
			var passwordErrors = this.validatePassword( model );
			var passwordConfirmationErrors = this.validatePasswordConfirmation( model );

			return _.union( loginErrors, nameErrors, passwordErrors, passwordConfirmationErrors );
		},

		validateLogin: function( model ) {
			var errors = [];

			var login = model.get( 'login' );
			if ( login == undefined || login == '' ) {
				errors.push( {
					field: 'login'
					, message: 'Enter login'
					, clazz: 'has-error'
				} );
			}

			return errors;
		},

		validateName: function( model ) {
			var errors = [];

			var name = model.get( 'name' );
			if ( name == undefined || name == '' ) {
				errors.push( {
					field: 'name'
					, message: 'Enter name'
//					, control: this.$( '.form-group.name' )
					, clazz: 'has-error'
				} );
			}

			return errors;
		},

		validatePassword: function( model ) {
			var errors = [];

			var password = model.get( 'password' );
			if ( password == undefined || password == '' ) {
				errors.push( {
					field: 'password'
					, message: 'Password can not be null'
//					, control: this.$( '.form-group.password' )
					, clazz: 'has-error'
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
				 	field: 'password'
					, message: 'Entered passwords are not equal'
//					, control: this.$( '.form-group.password_confirmation' )
					, clazz: 'has-error'
				} );
				errors.push( {
					field: 'password_confirmation'
					, message: ''
//					, control: this.$( '.form-group.password_confirmation' )
					, clazz: 'has-error'
				} );
			}

			return errors;
		}
	}
});