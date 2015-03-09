define( ["backbone"], function ( Backbone ) {

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		betMode: false,

		defaults: {
			matchId: 0
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
			this.url = '/rest/matches/';
		},

		refresh: function( data ) {
			this.fetch( { data: data, cache: false, reset: true } );
		}
	});

	return { MatchModel: MatchModel, MatchesModel: MatchesModel };
} );

