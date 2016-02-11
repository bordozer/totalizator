define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Collection.extend( {

		defaults: {},

		initialize: function ( options ) {
			this.onDate = options.options.onDate;
			this.onDateTo = options.options.onDateTo;
		},

		url: function () {
			return '/rest/portal-page/users-rating/';
		},

		refresh: function() {
			var data = {
				portalPageDate: this.onDate,
				onDateTo: this.onDateTo
            };
            this.fetch( { data: data }, { cache: false } );
		}
	} );
} );