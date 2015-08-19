define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		defaults: {
			matchId: 0
		},

		initialize: function ( options ) {
		}
	});

	var MatchesModel = Backbone.Collection.extend( {

		model: MatchModel,

		initialize: function ( options ) {
		},

		url: function() {
			return '/rest/matches/bets/'; // TODO: get IDs only?
		},

		refresh: function( data ) {
			this.fetch( { data: data, cache: false, reset: true} );
		}
	});

	return { MatchModel: MatchModel, MatchesModel: MatchesModel };
} );

