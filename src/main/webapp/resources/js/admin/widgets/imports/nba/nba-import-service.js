define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		error: 'Error' // TODO
	} );

	return {

		startImport: function( cupId ) {

			var result = {};
			$.ajax( {
				method: 'get',
				url: '/admin/rest/games-data-import/nba/start/?cupId=' + cupId,
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( translator.error );
				}
			} );

			return result;
		},

		stopImport: function() {

			var result = {};
			$.ajax( {
				method: 'get',
				url: '/admin/rest/games-data-import/nba/stop/',
				async: false,
				success: function ( response ) {
					result = response;
				},
				error: function() {
					alert( translator.error );
				}
			} );

			return result;
		},

		getImportStatus: function() {
			var result = {};
			$.ajax( {
				method: 'get',
				url: '/admin/rest/games-data-import/nba/state/',
				async: false,
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