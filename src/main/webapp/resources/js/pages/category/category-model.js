define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {
		},

		initialize: function ( options ) {
			this.categoryId = options.options.categoryId;
		},

		url: function () {
			return '/rest/category/' + this.categoryId + '/';
		}
	} );
} );