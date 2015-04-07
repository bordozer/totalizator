define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var MatchBetsModel = Backbone.Model.extend( {

		defaults: {
			matchId: 0
			, team1: {}
			, team2: {}
			, matchBets: []
		},

		initialize: function ( options ) {
			this.matchId = options.options.matchId;
		},

		url: function() {
			return '/rest/matches/' + this.matchId + '/bets/';
		}
	});

	return { MatchBetsModel: MatchBetsModel };
} );
