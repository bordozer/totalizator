define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {
			winnersCount: 0
			, usersCupBets: []
		},

		initialize: function ( options ) {
			this.cupId = options.options.cupId;
		},

		url: function () {
			return '/rest/cups/' + this.cupId + '/winners/bets/';
		}
	} );
} );