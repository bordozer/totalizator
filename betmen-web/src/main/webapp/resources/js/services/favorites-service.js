define( function ( require ) {

	'use strict';

	var $ = require( 'jquery' );
	var _ = require( 'underscore' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		serverError: 'Server error'
	} );

	return {

		addToCategoryToFavoritesOfCurrentUser: function( categoryId ) {

			$.ajax( {
				method: 'POST',
				url: '/rest/favorites/categories/' + categoryId + '/',
				async: false,
				success: function ( response ) {

				},
				error: function() {
					alert( translator.serverError );
				}
			} )
		},

		removeCategoryFromFavoritesOfCurrentUser: function( categoryId ) {

			$.ajax( {
				method: 'DELETE',
				url: '/rest/favorites/categories/' + categoryId + '/',
				async: false,
				success: function ( response ) {

				},
				error: function() {
					alert( translator.serverError );
				}
			} )
		}
	}
});