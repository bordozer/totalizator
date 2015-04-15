define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var Team = Backbone.Model.extend( {

		defaults: {
		},

		initialize: function ( options ) {
		}
	} );

	return Backbone.Collection.extend( {

		model: Team,

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