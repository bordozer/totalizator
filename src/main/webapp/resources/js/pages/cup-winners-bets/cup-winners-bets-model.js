define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {
			winnersCount: 0
			, cupBets: []
		},

		initialize: function ( options ) {
			this.matchId = options.options.matchId;
		},

		url: function () {
			return '/rest/cups/' + this.matchId + '/winners/bets/';
		}
	} );
} );