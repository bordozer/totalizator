define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var service = require( '/resources/js/services/service.js' );
	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	return Backbone.Model.extend( {

		defaults: {
			selectedSportKindId: 0
			, cupId: 0
			, timePeriod: {}
		},

		initialize: function ( options ) {

			this.sportKinds = service.loadSportKinds();

			var defaultSportKindId = this.sportKinds[ 0 ].sportKindId;
			this.set( { selectedSportKindId: defaultSportKindId } );
		}
	} );
} );

