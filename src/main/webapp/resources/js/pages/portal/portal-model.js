define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	return Backbone.Model.extend( {

		portalPageDate: {},

		defaults: {
			cupsToShow: []
			, cupsTodayToShow: []
		},

		initialize: function ( options ) {

			this.options = options.options;

			this.url = '/rest/portal-page/';

			this.portalPageDate = options.options.onDate || dateTimeService.dateNow();
		},

		refresh: function() {
			this.fetch( { data: { portalPageDate: this.portalPageDate } }, { cache: false } );
		},

		previousDay: function() {
			this.portalPageDate = dateTimeService.formatDate( dateTimeService.minusDay( this.portalPageDate ) );
		},

		nextDay: function() {
			this.portalPageDate = dateTimeService.formatDate( dateTimeService.plusDay( this.portalPageDate ) );
		}
	});
} );
