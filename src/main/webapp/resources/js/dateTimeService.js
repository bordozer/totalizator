define( function ( require ) {

	'use strict';

	var DATE_TIME_FORMAT = 'D/M/YYYY HH:mm';
	var DATE_TIME_DISPLAY_FORMAT = 'D MMM HH:mm';

	var $ = require( 'jquery' );
	var moment = require( 'moment' );

	return {

		formatDate: function ( time ) {
			return moment( time ).format( DATE_TIME_FORMAT );
		},

		formatDateDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( DATE_TIME_DISPLAY_FORMAT );
		},

		parseDate: function( time ) {
			return new Date( moment( time, DATE_TIME_FORMAT ).valueOf() );
		}
	}
} );
