define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	return Backbone.Model.extend( {

		portalPageDate: {},

		defaults: {
			cupsToShow: []
			, cupsTodayToShow: []
		},

		initialize: function ( options ) {

			this.options = options.options;

			this.url = '/rest/portal-page/';
		},

		refresh: function( onDate ) {
			this.fetch( { data: { portalPageDate: onDate } }, { cache: false } );
		}
	});
} );
