define( ["backbone"], function ( Backbone ) {

	var BetModel = Backbone.Model.extend( {

		idAttribute: 'betId',

		defaults: {
			betId: 0
			, matchId: 0
			, score1: 0
			, score2: 0
		},

		initialize: function ( options ) {
		}
	});

	var BetsModel = Backbone.Collection.extend( {

		model: BetModel,

		initialize: function ( options ) {
			this.url = '/rest/matches/open/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { BetsModel: BetsModel, BetModel: BetModel };
} );

