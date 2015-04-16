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

		startImport: function() {

			var result = {};
			$.ajax( {
				method: 'get',
				url: '/admin/rest/imports/nba/start/',
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
				url: '/admin/rest/imports/nba/stop/',
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