define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {},

		initialize: function ( options ) {
			this.team1Id = options.team1.teamId;
			this.team2Id = options.team2.teamId;
		},

		url: function () {
			return '/rest/teams/' + this.team1Id + '/vs/' + this.team2Id + '/';
		}
	} );
} );