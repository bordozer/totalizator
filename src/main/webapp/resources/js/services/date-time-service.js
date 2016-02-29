define( function ( require ) {

	'use strict';

	var DATE_FORMAT = 'DD/MM/YYYY';
	var TIME_FORMAT = 'HH:mm';

	var DATE_DISPLAY_FORMAT = 'D MMM YYYY';
	var TIME_DISPLAY_FORMAT = 'HH:mm';

	var DATE_DISPLAY_FORMAT_FULL = 'D MMMM YYYY, dddd';

	var app = require( 'app' );
	var LOCALE = app.language().country; // uk, ru, en

	var moment = require( 'moment' );

	return {

		getDateTimeFormat: function () {
			return DATE_FORMAT + ' ' + TIME_FORMAT;
		},

		getDateFormat: function () {
			return DATE_FORMAT;
		},

		getLocale: function () {
			return LOCALE;
		},

		// date => str formatted
		formatDate: function ( time ) {
			return moment( this.parseDate( time ) ).format( this.getDateFormat() );
		},

		// date => str formatted
		formatDateDayAndMonthOnly: function ( time ) {
			return moment( this.parseDate( time ) ).format( 'D MMM' );
		},

		// date => str formatted
		formatDateTime: function ( time ) {
			return moment( time ).format( this.getDateTimeFormat() );
		},

		// str => srt formatted for displaying
		formatDateTimeDisplay: function ( time ) {
			return this.formatDateDisplay( time ) + " <sup>" + this.formatTimeDisplay( time ) + "</sup>";
		},

		// str => srt formatted for displaying
		formatDateTimeFullDisplay: function ( time ) {
			return this.formatDateFullDisplay( time ) + " <sup>" + this.formatTimeDisplay( time ) + "</sup>";
		},

		formatMomentDateTimeDisplay: function ( momentTime ) {
			return momentTime.format( DATE_DISPLAY_FORMAT + ' ' + TIME_DISPLAY_FORMAT );
		},

		formatDateDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( DATE_DISPLAY_FORMAT );
		},

		formatDateFullDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( DATE_DISPLAY_FORMAT_FULL );
		},

		formatTimeDisplay: function ( time ) {
			return moment( this.parseDate( time ) ).format( TIME_DISPLAY_FORMAT );
		},

		// str => date
		parseDate: function ( time ) {
			return moment( time, this.getDateTimeFormat() ).toDate();
		},

		daysOffset: function ( days ) {
			return moment( app.timeNow() ).add( days, 'day' );
		},

		monthsOffset: function ( months ) {
			return moment( app.timeNow() ).add( months, 'month' );
		},

		startOfMonth: function ( momentDate ) {
			return moment( [ momentDate.get( 'year' ), momentDate.get( 'month' ) ] );
		},

		endOfMonth: function ( momentDate ) {
			return momentDate.endOf( 'month' );
		},

		daysRemainsTo: function ( momentDate ) {
			return momentDate.diff( this.timeNow(), 'days' );
		},

		monthAndYearDisplay: function( momentDate ) {
			return moment.months( momentDate.get( 'month' ) ) + ' ' + momentDate.get( 'year' );
		},

		minusDay: function( date ) {
			return this.minusDays( date, 1 );
		},

		minusDays: function( date, days ) {
			return moment( this.parseDate( date ) ).subtract( days, 'day' );
		},

		plusDay: function( date ) {
			return this.plusDays( date, 1 );
		},

		plusDays: function( date, days ) {
			return moment( this.parseDate( date ) ).add( days, 'day' );
		},

		timeNow: function () {
			return app.timeNow();
		},

		dateNow: function () {
			return moment( app.timeNow(), this.getDateFormat() ).format( this.getDateFormat() );
		},

		fromNow: function ( time ) {
			return moment.duration( moment( time, this.getDateTimeFormat() ).diff( moment( this.timeNow(), this.getDateTimeFormat() ) ) ).humanize( {
				suffix: true,
				precise: true
			} );
		},

		year: function(date) {
			return moment(date, this.getDateFormat()).get('year');
		},

		month: function(date) {
			return moment(date, this.getDateFormat()).get('month') + 1;
		},

		day: function(date) {
			return moment(date, this.getDateFormat()).get('date');
		},

		toMomentDate: function(strDate ) {
			return moment(strDate, this.getDateFormat());
		}
	}
} );
