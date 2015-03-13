define( function ( require ) {

	'use strict';

	// http://eonasdan.github.io/bootstrap-datetimepicker/Functions/

	var SELECTOR = '.date-picker-input';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var datetimepicker = require( 'datetimepicker' );

	var Template = require( 'text!./templates/datepicker-template.html' );

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
				format: 'D/M/YYYY HH:mm'
				, locale: 'ru'
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