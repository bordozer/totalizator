define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		initialize: function ( options ) {
			this.cup = options.options.cup;
		},

		url: function () {
			return '/rest/cups/' + this.cup.cupId + '/teams/';
		},

		load: function() {
			this.fetch( { cache:false } );
		},

		loadStartedWith: function( letter ) {
			this.fetch( { data: { letter: letter }, cache:false } );
		}
	} );
} );