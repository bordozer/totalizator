define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/date-picker-template.html' ) );

	var DateTimePickerView = require( 'js/controls/date-time-picker/date-time-picker' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.matchesOnDate = options.matchesOnDate;
			this.render();
		},

		render: function () {

			var data = _.extend( {}, { translator: translator } );
			this.$el.html( template( data ) );

			new DateTimePickerView( {
				el: this.$( '.datetimepicker' )
				, initialValue: this.matchesOnDate
				, disableTime: true
				, inline: true
				, datTimeChangeCallback: this._onDateSelect.bind( this )
			} );

			return this;
		},

		_onDateSelect: function( date ) {
			this.trigger( 'events:change_match_date', date );
		}
	} );
} );