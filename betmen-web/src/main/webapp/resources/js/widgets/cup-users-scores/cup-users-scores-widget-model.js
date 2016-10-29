define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		cup: {},
		userGroupId: 0,

		initialize: function ( options ) {
			this.cup = options.cup;
		},

		url: function() {
			return '/rest/cups/' + this.cup.cupId + '/scores/?userGroupId=' + this.userGroupId;
		},

		refresh: function() {
			this.fetch( { cache: false, reset: false } );
		}
	});
} );