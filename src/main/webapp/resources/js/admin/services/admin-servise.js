define( function ( require ) {

	'use strict';

	var $ = require( 'jquery' );
	var _ = require( 'underscore' );

	return {

		loadCups: function() {

			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/cups/',
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

		loadAllCurrentCups: function() {

			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/cups/current/',
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
		},

		setTeamCupParticipation: function( cupId, teamId, isTeamActiveInCup ) {
			$.ajax( {
				method: 'POST',
				url: '/admin/rest/teams/' + teamId + '/cups/' + cupId + '/active/' + isTeamActiveInCup + '/',
				async: false,
				success: function ( response ) {

				}
			} )
		}
	}
} );