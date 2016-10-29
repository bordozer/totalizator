define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		teamId: 0,

		defaults: {
			team: {}
		},

		initialize: function ( options ) {
			this.teamId = options.options.teamId;
		},

		url: function() {
			return '/rest/teams/' + this.teamId + '/card/';
		}
	});
} );

