define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		defaults: {
			cupsToShow: []
		},

		initialize: function ( options ) {
			this.cups = options.options.cups;
			this.team1 = options.options.team1;
			this.team2 = options.options.team2;
		},

		url: function() {
			return '/rest/teams/' + this.team1.teamId + '/vs/' + this.team2.teamId + '/';
		}
	} );
} );
