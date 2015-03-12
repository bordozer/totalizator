define( function ( require ) {

	'use strict';

	// http://eonasdan.github.io/bootstrap-datetimepicker/Functions/

	var SELECTOR = '.date-picker-input';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var datetimepicker = require( 'datetimepicker' );

	var Template = require( 'text!./templates/datepicker-template.html' );

	var dateTimeService = require( '/resources/js/dateTimeService.js' );

	return Backbone.View.extend( {

		template: _.template( Template ),

		initialize: function ( options ) {
			this.model = new Backbone.Model();
			this.render( options.initialValue );
		},

		render: function( time ) {

			this.$el.html( this.template( {
				time: dateTimeService.formatDateTime( time )
			} ) );

			var dtp = this.$( '.date-time-picker-container' ).datetimepicker( {
				format: 'D/M/YYYY'
				, locale: 'ru'
			} );

			return this;
		},

		setValue: function( datetime ) {
			return this.$( SELECTOR ).val( dateTimeService.formatDateTime( datetime ) );
		},

		getValue: function() {
//			console.log( this.$( SELECTOR ).val() );
			return new Date( this.$( SELECTOR ).val() );
		}
	});
} );