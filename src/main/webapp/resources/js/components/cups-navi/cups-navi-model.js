define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Collection.extend( {

		initialize: function ( options ) {
			this.url = '/rest/cups/current/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: false } );
		}
	});
} );
