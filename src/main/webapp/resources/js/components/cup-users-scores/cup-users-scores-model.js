define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		cupId: 0,

		initialize: function ( options ) {
			this.cupId = options.cupId;
		},

		url: function() {
			return '/rest/cups/' + this.cupId + '/scores/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: false } );
		}
	});
} );