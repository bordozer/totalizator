define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/rest/sport-kinds/cups/active/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: false } );
		}
	});
} );
