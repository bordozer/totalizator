define( function ( require ) {

	'use strict';

	var Translator = require( 'public/js/translator' );
	var translator = new Translator( {
		logoutConfirmationLabel: 'Logout confirmation: Logout?'
	} );

	return {

		logout: function () {

			if ( ! confirm( translator.logoutConfirmationLabel ) ) {
				return;
			}

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
		},

		reloadTranslations: function() {
			$.ajax( {
				method: 'GET',
				url: '/admin/translations/reload/',
				success: function ( response ) {
					window.location.reload();
				}
			} )
		}
	}
} );