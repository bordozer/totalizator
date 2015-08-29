define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		initialize: function ( options ) {
			this.initialFilter = options.filter;
		},

		url: function() {
			return '/rest/cups/' + this.initialFilter.cupId + '/matches/filter-data/';
		}
	});
} );
