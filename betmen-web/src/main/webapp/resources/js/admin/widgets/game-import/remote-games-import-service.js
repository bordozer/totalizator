define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		error: 'Can not get data from server'
	} );

	return {

		loadRemoteGameIds: function( importParameters ) {

			var result = [];

			$.ajax( {
				method: 'get',
				url: '/admin/rest/remote-games-import/collect-game-data-ids/',
				async: false,
				data: importParameters,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					console.log( translator.error );
				}
			} );

			return result;
		},

		loadCupsConfiguredForRemoteGameImport : function( sportKindId ) {
			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/cups/configured-for-remote-games-import/?sportKindId=' + sportKindId,
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					console.log( translator.error );
				}
			} );

			return result;
		},

		loadAllCurrentCupsConfiguredForRemoteGameImport : function( sportKindId ) {
			var result = [];

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/cups/configured-for-remote-games-import/current/?sportKindId=' + sportKindId,
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					console.log( translator.error );
				}
			} );

			return result;
		}
	}
} );