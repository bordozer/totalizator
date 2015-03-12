define( function ( require ) {

	'use strict';

	// http://eonasdan.github.io/bootstrap-datetimepicker/Functions/

	var SELECTOR = '.datepicker-template.html';

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
				time: time
			} ) );

			this.$( '.date-time-picker-container' ).datetimepicker( {
				locale: 'ru'
			} );

			return this;
		},

		setValue: function( datetime ) {
			return this.$( SELECTOR ).val( dateTimeService.formatDateTime( datetime ) );
		},

		getDate: function( datetime ) {
			return dateTimeService.formatDate( this._getValueStr() );
		},

		getTime: function( datetime ) {
			return dateTimeService.formatTime( this._getValueStr() );
		},

		getDateTime: function( datetime ) {
			return dateTimeService.formatDateTime( this._getValueStr() );
		},

		_getValueStr: function() {
			return this.$( SELECTOR ).val();
		}
	});
} );