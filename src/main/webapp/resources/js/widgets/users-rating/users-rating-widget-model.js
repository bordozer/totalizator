define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Collection.extend( {

		defaults: {},

		initialize: function ( options ) {
			this.portalPageDate = options.options.portalPageDate;
		},

		url: function () {
			return '/rest/portal-page/users-rating-on-date/';
		},

		refresh: function() {
			this.fetch( { data: { portalPageDate: this.portalPageDate } }, { cache: false } );
		}
	} );
} );