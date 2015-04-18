define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var MatchModel = Backbone.Model.extend( {

		idAttribute: 'matchId',

		categoryId: 0,
		_selected: false,

		defaults: {
			matchId: 0
			, categoryId: 0
			, cupId: 0
			, team1Id: 0
			, score1: 0
			, team2Id: 0
			, score2: 0
			, beginningTime: new Date()
			, matchFinished: false
		},

		initialize: function ( options ) {
		},

		url: function() {
			return '/admin/rest/matches/' + this.get( 'matchId' );
		},

		selected: function( val ) {

			if ( val == undefined ) {
				return this._selected;
			}

			this._selected = val;
		},

		finish: function() {
			this.save( { matchFinished: true }, { async: false } );
		},

		deleteMatch: function() {
			this.destroy();
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
