define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		categoryId: 0,
		_selected: false,

		defaults: {
			matchId: 0
			, category: {}
			, cup: {}
			, team1: {}
			, score1: 0
			, team2: {}
			, score2: 0
			, beginningTime: new Date()
			, matchFinished: false
		},

		initialize: function ( options ) {

		},

		selected: function( val ) {

			if ( val == undefined ) {
				return this._selected;
			}

			this._selected = val;
		},

		finish: function() {
			this.save( { matchFinished: true }, { async: false } );
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
