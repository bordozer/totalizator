define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		defaults: {
		},

		initialize: function ( options ) {
			this.team1Id = options.options.team1Id;
			this.team2Id = options.options.team2Id;
		},

		url: function() {
			return '/rest/teams/' + this.team1Id + '/vs/' + this.team2Id + '/';
		}
	} );
} );
