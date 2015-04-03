define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		betMode: false,

		defaults: {
			matchId: 0
			, bettingAllowed: false
		},

		initialize: function ( options ) {
		},

		setModeBet: function() {
			this.betMode = true;
		},

		setModeMatchInfo: function() {
			this.betMode = false;
		},

		isBetMode: function() {
			return this.betMode;
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
			this.url = '/rest/matches/';
		},

		refresh: function( data ) {
			this.fetch( { data: data, cache: false, reset: true} );
		}
	});

	return { MatchModel: MatchModel, MatchesModel: MatchesModel };
} );

