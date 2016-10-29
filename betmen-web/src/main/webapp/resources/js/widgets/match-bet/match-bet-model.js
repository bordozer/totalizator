define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	return Backbone.Model.extend( {

		idAttribute: 'matchId',

		defaults: {
			matchId: 0
			, bettingAllowed: false
		},

		initialize: function ( options ) {
			this.matchId = options.options.matchId;
			this.showBetForUserId = options.options.showBetForUserId;
		},

		url: function() {
			return '/rest/matches/' + this.matchId + '/bet-of-user/' + this.showBetForUserId + '/';
		},

		isBettingAllowed: function() {
			return this.get( 'bettingAllowed' );
		},

		resetBet: function() {
			this.set( { bet: null } );
		}
	});
} );


