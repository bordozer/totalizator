define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		userId: 0,

		defaults: {
			users: []
		},

		initialize: function ( options ) {
		},

		url: function() {
			return '/rest/users/';
		}
	});
} );
