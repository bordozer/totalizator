define( function ( require ) {

	'use strict';

	var $ = require( 'jquery' );
	var moment = require( 'moment' );

	return {

		formatDate: function ( time ) {
			return moment( time ).format( 'D/M/YYYY HH:mm' );
		}
	}
} );
