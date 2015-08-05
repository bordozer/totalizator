define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		defaults: {
			matchId: 0
			, bettingAllowed: false
		},

		initialize: function ( options ) {
		},

		isBettingAllowed: function() {
			return this.get( 'bettingAllowed' );
		},

		resetBet: function() {
			this.set( { bet: null } );
		}
	});

	var MatchesModel = Backbone.Collection.extend( {

		model: MatchModel,

		initialize: function ( options ) {
			this.url = '/rest/matches/bets/';
		},

		refresh: function( data ) {
			this.fetch( { data: data, cache: false, reset: true} );
		}
	});

	return { MatchModel: MatchModel, MatchesModel: MatchesModel };
} );

