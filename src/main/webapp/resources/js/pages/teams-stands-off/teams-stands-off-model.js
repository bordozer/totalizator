define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		defaults: {
			cupsToShow: []
			, matchesAndBets: []
		},

		initialize: function ( options ) {
			this.cups = options.options.cups;
			this.team1Id = options.options.team1Id;
			this.team2Id = options.options.team2Id;
		}

		/*url: function() {
			return '/rest/teams/standoff/' + this.team1Id + '/vs/' + this.team2Id + '/';
		}*/
	} );
} );
