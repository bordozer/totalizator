define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var MatchBetModel = Backbone.Collection.extend( {

		initialize: function ( options ) {

		}
	});

	var MatchBetsModel = Backbone.Collection.extend( {

		model: MatchBetModel,

		initialize: function ( options ) {
			this.matchId = options.options.matchId;
		},

		url: function() {
			return '/rest/matches/' + this.matchId + '/bets/';
		}
	});

	return { MatchBetsModel: MatchBetsModel, MatchBetModel: MatchBetModel };
} );
