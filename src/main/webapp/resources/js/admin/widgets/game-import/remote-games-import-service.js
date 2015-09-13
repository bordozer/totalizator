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
					alert( translator.error );
				}
			} );

			return result;
		},

		loadLocalDataForRemoteGame: function( remoteGame, cupId ) {

			var result = [];

			$.ajax( {
				method: 'get',
				url: '/admin/rest/remote-games-import/remote-game/local-data/?cupId=' + cupId,
				async: false,
				data: remoteGame,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( translator.error );
				}
			} );

			return result;
		}
	}
} );