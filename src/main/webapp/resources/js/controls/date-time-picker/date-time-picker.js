define( function ( require ) {

	'use strict';

	// http://eonasdan.github.io/bootstrap-datetimepicker/Functions/

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var datetimepicker = require( 'datetimepicker' );

	var Template = require( 'text!./templates/date-time-picker-template.html' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var callback = function() {};

	return Backbone.View.extend( {

		template: _.template( Template ),

		initialize: function ( options ) {
			this.model = new Backbone.Model();

			this.render( options.initialValue, options.disableTime, options.datTimeChangeCallback || callback );
		},

		render: function( time, disableTime, onChangeCallback ) {

			this.$el.html( this.template( {
			} ) );

			this.dtp = this.$( '.date-time-picker-container' ).datetimepicker( {
				format: disableTime ? dateTimeService.getDateFormat() : dateTimeService.getDateTimeFormat()
				, locale: dateTimeService.getLocale()
			} ).on( "dp.change", function ( e ) {
					onChangeCallback( dateTimeService.formatDate( e.date ) );
				} );

			this.picker = this.dtp.data( "DateTimePicker" );
			this.setValue( time );

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