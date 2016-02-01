define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var StandoffModel = require( './standoff-model' );

	var template = _.template( require( 'text!./templates/standoff-template.html' ) );

	var MatchTransformer = require( 'js/widgets/match-bet/match-transformer' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		won: "Won games"
		, lost: "Lost games"
		, total: "Total games"
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {
			this.cup = options.cup;
			this.match = options.match;
			this.team1 = options.team1;
			this.team2 = options.team2;

			this.model = new StandoffModel(options);

			this.model.on( 'sync', this.renderStandoffs, this );
			this.render();
		},

		render: function () {
			this.model.fetch( { cache: false } );
		},

		renderStandoffs: function () {

			var jmodel = this.model.toJSON();

			var matchesMap = this._getMatchesMap( jmodel.standoffsByCup, this.cup, this.team1, this.team2  );

			var data = _.extend( {}, jmodel, {
				match: this.match
				, team1: this.team1
				, team2: this.team2
				, matchesMap: matchesMap
				, wonLost: this.loadTeamsLostWonStatistics( this.cup.cupId, this.team1.teamId, this.team2.teamId )
				, translator: translator
			} );

			this.$el.html( template( data ) );

			return this;
		},

		_getMatchesMap: function( standoffsByCup, cup, team1, team2 ) {

			if ( standoffsByCup == null || standoffsByCup == undefined || standoffsByCup.length == 0 ) {
				return {};
			}


			var mapper = function ( match ) {
				return new MatchTransformer( match, null, team1.teamId, team2.teamId );
			};

			var ret = {};

			var first = standoffsByCup[ 0 ].cup;
			ret[ first.cupId ] = _.map( service.loadTeamsCupMatches( first.cupId, team1.teamId, team2.teamId ), mapper );

			if ( standoffsByCup.length > 1 ) {
				var second = standoffsByCup[ 1 ].cup;
				ret[ second.cupId ] = _.map( service.loadTeamsCupMatches( second.cupId, team1.teamId, team2.teamId ), mapper );
			}

			return ret;
		},

		loadTeamsLostWonStatistics: function( cupId, team1Id, team2Id ) {
			var result = {};

			$.ajax( {
				method: 'GET',
				url: '/rest/teams/team1/' + team1Id + '/team2/' + team2Id + '/statistics/cup/' + cupId + '/',
				async: false,
				success: function ( data ) {
					result = data;
				},
				error: function() {
					alert( translator.serverError );
				}
			} );

			return result;
		}
	} );
} );