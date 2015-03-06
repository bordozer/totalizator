define( ["backbone"], function ( Backbone ) {

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		categoryId: 0,

		defaults: {
			matchId: 0
			, categoryId: 0
			, categoryName: ''
			, cupId: 0
			, cupName: ''
			, team1Id: 0
			, team1Name: ''
			, score1Id: 0
			, team2Id: 0
			, team2Name: ''
			, score2Id: 0
			, lastBetTime: {}
		},

		initialize: function ( options ) {

		}
	});

	var MatchesModel = Backbone.Collection.extend( {

		model: MatchModel,

		initialize: function ( options ) {
			this.url = '/admin/rest/matches/';
		}
	});

	return { MatchesModel: MatchesModel, MatchModel: MatchModel };
} );
