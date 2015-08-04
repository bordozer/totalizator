define( function ( require ) {

	'use strict';

	var DATE_TIME_FORMAT = 'DD/MM/YYYY HH:mm';
	var DATE_FORMAT = 'DD/MM/YYYY';

	var DATE_DISPLAY_FORMAT = 'D MMM YYYY';
	var TIME_DISPLAY_FORMAT = 'HH:mm';

	var app = require( 'app' );
	var LOCALE = app.language().country; // uk, ru, en

	var moment = require( 'moment' );
	moment.locale( LOCALE, {
	} );

	return {

		getDateTimeFormat: function() {
			return DATE_TIME_FORMAT;
		},

		getDateFormat: function() {
			return DATE_FORMAT;
		},

		getLocale: function() {
			return LOCALE;
		},

		// date => str formatted
		formatDate: function ( time ) {
			return moment( time ).format( DATE_TIME_FORMAT );
		},

		// str => srt formatted for displaying
		formatDateTimeDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( DATE_DISPLAY_FORMAT + ' ' + TIME_DISPLAY_FORMAT );
		},

		formatDateDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( DATE_DISPLAY_FORMAT );
		},

		formatTimeDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( TIME_DISPLAY_FORMAT );
		},

		// str => date
		parseDate: function( time ) {
			return moment( time, DATE_TIME_FORMAT ).toDate();
		},

		fromNow: function( time ) {
			return moment( time, DATE_TIME_FORMAT ).fromNow();
		}
	}
} );
