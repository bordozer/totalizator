define( ["backbone"], function ( Backbone ) {

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		categoryId: 0,

		defaults: {
			matchId: 0
			, categoryId: 0
			, cupId: 0
			, team1Id: 0
			, score1: 0
			, team2Id: 0
			, score2: 0
			, lastBetTime: new Date()
		},

		initialize: function ( options ) {

		}
	});

	var MatchesModel = Backbone.Collection.extend( {

		model: MatchModel,

		initialize: function ( options ) {
			this.url = '/admin/rest/matches/';
		},

		refresh: function( data ) {
			this.fetch( { data: data, cache: false, reset: true } );
		}
	});

	return { MatchesModel: MatchesModel, MatchModel: MatchModel };
} );
