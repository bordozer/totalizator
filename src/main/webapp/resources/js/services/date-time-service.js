define( function ( require ) {

	'use strict';

	var DATE_TIME_FORMAT = 'DD/MM/YYYY HH:mm';
	var DATE_TIME_DISPLAY_FORMAT = 'D MMM HH:mm';

	var LOCALE = 'ru';

	var $ = require( 'jquery' );

	var moment = require( 'moment' );
	moment.locale( LOCALE, {
	} );

	return {

		getFormat: function() {
			return DATE_TIME_FORMAT;
		},

		getLocale: function() {
			return LOCALE;
		},

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
