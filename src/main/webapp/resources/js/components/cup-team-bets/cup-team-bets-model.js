define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {
			cupTeamBets: []
		},

		initialize: function ( options ) {
			this.cup = options.options.cup;
		},

		url: function() {
			return '/rest/cups/' + this.cup.cupId + '/bets/';
		}
	});
} );
