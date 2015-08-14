define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/date-picker-template.html' ) );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
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

			var callback = this._onDateSelect.bind( this );

			this.dateTimePickerView = new DateTimePickerView( {
				el: this.$( '.datetimepicker' )
				, initialValue: this.matchesOnDate
				, disableTime: true
				, inline: true
				, datTimeChangeCallback: callback
			} );

			return this;
		},

		_onDateSelect: function() {

			if ( ! this.dateTimePickerView ) { // TODO: why this.dateTimePickerView in undefined at first time?
				return;
			}

			this.trigger( 'events:change_match_date', dateTimeService.formatDateTime( this.dateTimePickerView.getValue() ) );
		}
	} );
} );