define( function ( require ) {

	'use strict';

	var DATE_FORMAT = 'DD/MM/YYYY';
	var TIME_FORMAT = 'HH:mm';

	var DATE_DISPLAY_FORMAT = 'D MMM YYYY';
	var TIME_DISPLAY_FORMAT = 'HH:mm';

	var app = require( 'app' );
	var LOCALE = app.language().country; // uk, ru, en

	var moment = require( 'moment' );
	moment.locale( LOCALE, {
	} );

	return {

		getDateTimeFormat: function() {
			return DATE_FORMAT + ' ' + TIME_FORMAT;
		},

		getDateFormat: function() {
			return DATE_FORMAT;
		},

		getLocale: function() {
			return LOCALE;
		},

		// date => str formatted
		formatDate: function ( time ) {
			return moment( time ).format( this.getDateFormat() );
		},

		// date => str formatted
		formatDateTime: function ( time ) {
			return moment( time ).format( this.getDateTimeFormat() );
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
			return moment( time, this.getDateTimeFormat() ).toDate();
		},

		timeNow: function() {
			return app.timeNow();
		},

		dateNow: function() {
			return moment( app.timeNow(), this.getDateFormat() ).format( this.getDateFormat() );
		},

		fromNow: function( time ) {
			return moment.duration( moment( time, this.getDateTimeFormat() ).diff( moment( this.timeNow(), this.getDateTimeFormat() ) ) ).humanize( {
				suffix: true,
				precise: true
			} );
			//return moment( time, DATE_TIME_FORMAT ).fromNow();
		}
	}
} );
