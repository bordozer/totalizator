define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/date-range-picker-template.html' ) );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var DateTimePickerView = require( 'js/controls/date-time-picker/date-time-picker' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		dateFromLabel: "Date from"
		, dateToLabel: "Date to"
		, dateRangeLabel: "Date range"
		, daysOffsetLabel: "Days offset"
		, monthsOffsetLabel: "Month offset"
	} );

	var TIME_PERIOD_TYPE_DATE_RANGE = 1;
	var TIME_PERIOD_TYPE_DAYS_OFFSET = 2;
	var TIME_PERIOD_TYPE_MONTH_OFFSET = 3;

	return Backbone.View.extend( {

		events: {
			"change input[name='time-period-type']": '_onTimePeriodTypeChange'
			, "change #days-offset": '_onDaysOffsetChange'
			, "change #months-offset": '_onMonthsOffsetChange'
		},

		initialize: function ( options ) {

			this.parameters = options.parameters;

			var today = dateTimeService.dateNow();

			this.parameters.dateFrom = this.parameters.dateFrom || today;
			this.parameters.dateTo = this.parameters.dateTo || today;

			this.parameters.daysOffset = this.parameters.daysOffset || 0;
			this.parameters.monthsOffset = this.parameters.monthsOffset || 0;

			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.parameters, {
				translator: translator
			} );

			this.$el.html( template( data ) );

			if ( this.parameters.timePeriodType == TIME_PERIOD_TYPE_DATE_RANGE ) {

				new DateTimePickerView( {
					el: this.$( '.js-date-from' )
					, initialValue: this.parameters.dateFrom
					, disableTime: true
					, datTimeChangeCallback: this._onDateFromSelect.bind( this )
				} );

				new DateTimePickerView( {
					el: this.$( '.js-to-date' )
					, initialValue: this.parameters.dateTo
					, disableTime: true
					, datTimeChangeCallback: this._onDateToSelect.bind( this )
				} );
			}

			if ( this.parameters.timePeriodType == TIME_PERIOD_TYPE_DAYS_OFFSET ) {
				this._onDaysOffsetChange();
			}

			if ( this.parameters.timePeriodType == TIME_PERIOD_TYPE_MONTH_OFFSET ) {
				this._onMonthsOffsetChange();
			}
		},

		getParameters: function() {
			return this.parameters;
		},

		_onTimePeriodTypeChange: function ( evt ) {
			this.parameters.timePeriodType = $( evt.target ).val();
			this.render();

			this.trigger( 'events:date_range_change', this.parameters );
		},

		_onDateFromSelect: function ( date ) {
			this.parameters.dateFrom = date;
			this.trigger( 'events:date_range_change', this.parameters );
		},

		_onDateToSelect: function ( date ) {
			this.parameters.dateTo = date;
			this.trigger( 'events:date_range_change', this.parameters );
		},

		_onDaysOffsetChange: function () {

			var daysOffset = this.$( '#days-offset' ).val();

			var dateTo = dateTimeService.daysOffset( daysOffset );

			this.parameters.dateFrom = dateTimeService.dateNow();
			this._setDateTo( dateTo );

			this.parameters.daysOffset = daysOffset;
			this.$( '.js-days-offset-date' ).text( dateTimeService.formatDateFullDisplay( dateTo ) );

			this.trigger( 'events:date_range_change', this.parameters );
		},

		_onMonthsOffsetChange: function () {

			var monthsOffset = this.$( '#months-offset' ).val();

			var dateAfterMonthsOffset = dateTimeService.monthsOffset( monthsOffset );

			var firstDayOfMonth = dateTimeService.startOfMonth( dateAfterMonthsOffset );
			var lastDayOfMonth = dateTimeService.endOfMonth( dateAfterMonthsOffset );

			var daysOffset = dateTimeService.daysRemainsTo( lastDayOfMonth );

			this._setDateFrom( firstDayOfMonth );
			this._setDateTo( lastDayOfMonth );
			this.parameters.daysOffset = daysOffset;
			this.parameters.monthsOffset = monthsOffset;

			this.$( '.js-months-offset-date' ).text( dateTimeService.monthAndYearDisplay( firstDayOfMonth ) );

			this.trigger( 'events:date_range_change', this.parameters );
		},

		_setDateFrom: function( momentDate ) {
			this.parameters.dateFrom = dateTimeService.formatDate( momentDate );
		},

		_setDateTo: function( momentDate ) {
			this.parameters.dateTo = dateTimeService.formatDate( momentDate );
		}
	} );
} );