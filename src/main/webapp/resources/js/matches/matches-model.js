define( ["backbone"], function ( Backbone ) {

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		betMode: false,

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
		},

		setModeBet: function() {
			this.betMode = true;
		},

		setModeMatchInfo: function() {
			this.betMode = false;
		},

		isBetMode: function() {
			return this.betMode;
		}
	});

	var MatchesModel = Backbone.Collection.extend( {

		model: MatchModel,

		initialize: function ( options ) {
			this.url = '/rest/matches/open/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { MatchModel: MatchModel, MatchesModel: MatchesModel };
} );

