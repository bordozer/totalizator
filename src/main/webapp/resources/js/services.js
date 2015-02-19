define( [ 'jquery' ], function ( $ ) {

	return {

		logout: function () {
			$.ajax( {
				method: 'POST',
				url: '/logout',
				/*headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					"X-Login-Ajax-call": 'true'
				},*/
				success: function ( response ) {
					window.location.reload();
				},
				error: function() {
					alert( 'Logout failed' ); // TODO
				}
			} )
		}
	}
} );