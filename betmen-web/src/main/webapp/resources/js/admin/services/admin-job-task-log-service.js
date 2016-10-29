define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		serverError: 'Server error'
	} );

	return {

		loadImportedByJobRemoteGameMatchPairsAsync: function( jobTaskLogId, callback ) {

			$.ajax( {
				method: 'GET',
				url: '/admin/rest/jobs/custom-results/games-import/logs/' + jobTaskLogId + '/imported-games/',
				async: true,
				cache: false,
				success: function ( data ) {
					callback( data );
				},
				error: function() {
					console.error( translator.serverError );
				}
			} );
		}
	}
} );