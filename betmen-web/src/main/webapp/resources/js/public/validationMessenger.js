define( [ 'jquery' ], function ( $ ) {

	return {

		addErrors: function( control, errors, messageContainer ) {

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
		}
	}
});