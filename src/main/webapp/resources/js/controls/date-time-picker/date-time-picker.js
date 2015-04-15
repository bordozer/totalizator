define( function ( require ) {

	'use strict';

	// http://eonasdan.github.io/bootstrap-datetimepicker/Functions/

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var datetimepicker = require( 'datetimepicker' );

	var Template = require( 'text!./templates/date-time-picker-template.html' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	return Backbone.View.extend( {

		template: _.template( Template ),

		initialize: function ( options ) {
			this.model = new Backbone.Model();
			this.render( options.initialValue );
		},

		render: function( time ) {

			this.$el.html( this.template( {
			} ) );

			this.dtp = this.$( '.date-time-picker-container' ).datetimepicker( {
				format: dateTimeService.getFormat()
				, locale: dateTimeService.getLocale()
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