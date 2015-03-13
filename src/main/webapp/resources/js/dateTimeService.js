define( function ( require ) {

	'use strict';

	var DATE_TIME_FORMAT = 'D/M/YYYY HH:mm';
	var DATE_TIME_DISPLAY_FORMAT = 'D MMM HH:mm';

	var $ = require( 'jquery' );

	var moment = require( 'moment' );
	moment.locale( 'ru', {
	} );

	return {

		// date => str formatted
		formatDate: function ( time ) {
			return moment( time ).format( DATE_TIME_FORMAT );
		},

		// str => srt formatted for displaying
		formatDateDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( DATE_TIME_DISPLAY_FORMAT );
		},

		// str => date
		parseDate: function( time ) {
			return moment( time, DATE_TIME_FORMAT ).toDate();
		}
	}
} );
