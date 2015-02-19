define( [ 'jquery' ], function ( $ ) {

	return {

		logout: function () {
			$.ajax( {
				method: 'POST'
				, url: '/logout'
				, success: function ( response ) {
					if ( response.status == 200 ) {
						window.location.reload();
					} else {
						alert( 'Logout failed!' ); // TODO
					}
				}
			} )
		}
	}
} );