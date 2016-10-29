define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	return Backbone.Model.extend( {

		defaults: {},

		initialize: function ( options ) {
		},

		url: function () {
			return '/rest/matches/bets/collapsed/';
		},

		refresh: function ( data ) {
			this.fetch( { data: data, cache: false, reset: true } );
		}
	} );
} );

