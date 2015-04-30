define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {
		},

		initialize: function ( options ) {
			this.cupId = options.options.cupId;
		},

		url: function () {
			return '/rest/cups/' + this.cupId + '/';
		}
	} );
} );