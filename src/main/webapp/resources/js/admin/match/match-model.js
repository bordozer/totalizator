define( ["backbone"], function ( Backbone ) {

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
			this.url = '/admin/matches/';
		}
	});

	return { MatchesModel: MatchesModel, MatchModel: MatchModel };
} );
