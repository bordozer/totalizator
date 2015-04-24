define( function ( require ) {

	'use strict';

	var $ = require( 'jquery' );
	var _ = require( 'underscore' );

	return {

		loadCups: function() {

			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/cups/all/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( 'Error loading cups' ); // TODO: translate
				}
			} );

			return result;
		},

		loadCup: function( cupId ) {
			var result = {};

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/cups/' + cupId + '/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( 'Error loading cups' ); // TODO: translate
				}
			} );

			return result;
		},

		reloadTranslations: function() {
			$.ajax( {
				method: 'GET',
				url: '/admin/rest/translations/reload/',
				success: function ( response ) {
//					window.location.reload();
				}
			} )
		}
	}
} );