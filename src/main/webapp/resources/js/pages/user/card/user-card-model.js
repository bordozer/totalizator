define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		userId: 0,

		defaults: {
			cupsToShow: []
		},

		initialize: function ( options ) {
			this.userId = options.options.userId;
		},

		url: function() {
			return '/rest/users/' + this.userId + '/card/';
		}
	});
} );
