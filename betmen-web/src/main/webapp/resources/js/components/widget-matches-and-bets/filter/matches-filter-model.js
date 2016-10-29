define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	return Backbone.Model.extend( {

		defaults: {
			userId: 0
			, categoryId: 0
			, cupId: 0
			, teamId: 0
			, team2Id: 0
			, filterByDate: dateTimeService.dateNow()
			, filterByDateEnable: false
			, showFutureMatches: false
			, showFinished: false
			, sorting: 1
		},

		initialize: function ( options ) {

			this.initialSettings = this.toJSON();

			this.saveAttributes();
		},

		saveAttributes: function() {
			this.appliedAttributes = this.toJSON();
		},

		restoreAttributes: function() {
			this.set( this.appliedAttributes );
		},

		reset: function() {
			this.set( this.initialSettings );
			this.saveAttributes();
		}
	});
});