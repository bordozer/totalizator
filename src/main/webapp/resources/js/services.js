define( [ 'jquery' ], function ( $ ) {

	return {

		logout: function () {
			$.ajax( {
				method: 'POST',
				url: '/logout',
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