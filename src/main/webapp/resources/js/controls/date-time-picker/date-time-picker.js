define( function ( require ) {

	'use strict';

	// http://eonasdan.github.io/bootstrap-datetimepicker/Functions/

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/date-time-picker-template.html' ) );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var datetimepicker = require( 'datetimepicker' );

	var callback = function() {};

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.model = new Backbone.Model();

			this.render( options.initialValue, options.disableTime, options.datTimeChangeCallback || callback, options.inline );
		},

		render: function( time, disableTime, onChangeCallback, inline ) {

			this.$el.html( template( {
				inline: inline
			} ) );

			this.dtp = this.$( '.date-time-picker-container' ).datetimepicker( {
				format: disableTime ? dateTimeService.getDateFormat() : dateTimeService.getDateTimeFormat()
				, locale: dateTimeService.getLocale()
				, inline: inline
			} ).on( "dp.change", function ( e ) {
				onChangeCallback( disableTime ? dateTimeService.formatDate( e.date ) : dateTimeService.formatDateTime( e.date ) );
			} );

			this.picker = this.dtp.data( "DateTimePicker" );
			this.setValue( time ); // TODO: set silently

			return this;
		},

		setValue: function( datetime ) {
			this.picker.date( datetime );
		},

		getValue: function() {
			var datetime = this.picker.date();
			return new Date( datetime.valueOf() );
		}
	});
} );