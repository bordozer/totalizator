define( function ( require ) {

	'use strict';

	var $ = require( 'jquery' );
	var ui = require( 'jquery_ui' );

//	var DateTimePickerView = require( 'js/components/datepicker/datepickerView' );

	return {

		formatDate: function ( time ) {
			return $.datepicker.formatDate( "dd/mm/yy", time );
		},

		formatTime: function ( time ) {
			return $.datepicker.formatDate( "HH:mm", time );
		},

		formatDateTime: function ( time ) {
			return this.formatDate( time ) + ' ' + this.formatTime( time );
		}

		/*createDateTimePickerView: function( el, time ) {
			return new DateTimePickerView( { el: el, initialValue: time } );
		}*/
	}
} );
