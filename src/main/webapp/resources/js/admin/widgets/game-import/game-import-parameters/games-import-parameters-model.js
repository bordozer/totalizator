define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {
			cupId: 0
			, dateFrom: new Date()
			, dateTo: new Date()
		},

		initialize: function ( options ) {

		}
	} );
} );

