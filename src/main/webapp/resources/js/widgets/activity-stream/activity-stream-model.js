define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Collection.extend( {

		initialize: function ( options ) {

		},

		url: function () {
			return '/rest/activity-stream/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	} );
} );