define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Collection.extend( {

		initialize: function ( options ) {
			this.matchId = options.options.matchId;
			this.userId = options.options.userId;
		},

		url: function () {

			if ( this.matchId ) {
				return '/rest/activity-stream/matches/' + this.matchId + '/';
			}

			if ( this.userId ) {
				return '/rest/activity-stream/users/' + this.userId + '/';
			}

			return '/rest/activity-stream/portal/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	} );
} );