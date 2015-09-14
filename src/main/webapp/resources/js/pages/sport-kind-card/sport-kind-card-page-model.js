define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Collection.extend( {

		defaults: {},

		initialize: function ( options ) {
			this.sportKindId = options.options.sportKindId
		},

		url: function() {
			return '/rest/sport-kinds/' + this.sportKindId + '/cups/';
		}
	} );
} );