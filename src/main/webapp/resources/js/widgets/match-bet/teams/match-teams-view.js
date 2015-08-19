define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-teams-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		matchHasNotFinishedYet: "The match has not finished yet"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {

			this.match = options.match;

			this.render();
		},

		render: function () {

			var match = this.match;
			var isMatchFinished = match.matchFinished;

			var data = _.extend( {}, {
				match: match
				, matchPoints: this._matchPoints()
			} );

			this.$el.html( template( data ) );
		},

		_matchPoints: function() {

			var match = this.match;
			var isMatchFinished = match.matchFinished;

			if ( isMatchFinished ) {

				var matchResults = service.matchResultsByMatch( match );

				return {
					score1: "<span class='" + matchResults.style1 + "'>" + match.score1 + "</span>"
					, score2: "<span class='" + matchResults.style1 + "'>" + match.score2 + "</span>"
				}
			}

			var score = "<span class='text-muted' title='" + translator.matchHasNotFinishedYet + "'>?</span>";

			return { score1: score, score2: score }
		}
	} );
} );