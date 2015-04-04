define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.Model.extend( {

		defaults: {
			id: 0
			, userId: 0
			, userName: ''
		},

		initialize: function ( options ) {
			this.options = options.options;
		},

		url: function() {
			return '/rest/user/' + this.options.userId + '/';
		}
	});
} );
