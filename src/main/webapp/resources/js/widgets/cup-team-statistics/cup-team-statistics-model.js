define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		initialize: function ( options ) {
			this.team = options.options.team;
			this.cup = options.options.cup;
		},

		url: function() {
			return '/rest/teams/' + this.team.teamId + '/cup/' + this.cup.cupId + '/statistics/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: false } );
		}
	});
} );
