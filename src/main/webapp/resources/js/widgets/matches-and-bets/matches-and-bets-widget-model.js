define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var MatchModel = Backbone.Model.extend( {

		defaults: {
			date: 0
			, matchIds: []
		},

		initialize: function ( options ) {
		}
	} );

	var MatchesModel = Backbone.Collection.extend( {

		model: MatchModel,

		initialize: function ( options ) {
		},

		url: function () {
			return '/rest/matches/bets/';
		},

		refresh: function ( data ) {
			this.fetch( { data: data, cache: false, reset: true } );
		}
	} );

	return { MatchModel: MatchModel, MatchesModel: MatchesModel };
} );

