define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/games-import-job-parameters-template.html' ) );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );
	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		cupLabel: "Cup"
		, sportKindLabel: "Sport kind"
		, categoryLabel: "Category"
		, matchesLabel: "Matches"
		, dateFromLabel: "Date from"
		, dateToLabel: "Date to"
		, dateRangeLabel: "Date range"
		, daysOffsetLabel: "Days offset"
		, monthsOffsetLabel: "Month offset"
		, gamesLabel: "Matches"
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.parameters = options.parameters;
			this.cup = adminService.loadCup( this.parameters.cupId );
		},

		render: function () {

			var timePeriod = this.parameters.timePeriod;
			var timePeriodType = timePeriod.timePeriodType;

			var data = _.extend( {}
					, this.parameters
					, {
						cup: this.cup
						, timePeriod: timePeriod
						, timePeriodType: timePeriodType
						, dateFromFormatted: dateTimeService.formatDateFullDisplay( timePeriod.dateFrom )
						, dateToFormatted: dateTimeService.formatDateFullDisplay( timePeriod.dateTo )
						, daysOffsetText: timePeriodType == 2 ? dateTimeService.formatDateFullDisplay( dateTimeService.daysOffset( timePeriod.daysOffset ) ) : 0
						, monthsOffsetText: timePeriodType == 3 ? dateTimeService.monthAndYearDisplay( dateTimeService.monthsOffset( timePeriod.monthsOffset ) ) : 0
						, translator: translator
					} );

			this.$el.html( template( data ) );

			return this;
		}
	} );
} );