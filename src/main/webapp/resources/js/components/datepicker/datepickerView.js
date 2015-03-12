define( function ( require ) {

	// http://eonasdan.github.io/bootstrap-datetimepicker/Functions/

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	'use strict';

	var TemplateList = require( 'text!./templates/datepicker-template.html' );

	var datetimepicker = require( 'datetimepicker' );

	return Backbone.View.extend( {

		template: _.template( TemplateList ),

		initialize: function ( options ) {
			this.model = new Backbone.Model();

			this.render();
		},

		render: function() {

			this.$el.html( this.template( {
			} ) );

			this.$( '.date-time-picker-container' ).datetimepicker( {
				locale: 'ru'
			} );

			return this;
		},

		dateTime: function( datetime ) {
			if ( datetime === undefined ) {
				return this.$( '.datepicker-template.html' ).val();
			}

			return this.$( '.datepicker-template.html' ).val( datetime );
		}
	});
} );