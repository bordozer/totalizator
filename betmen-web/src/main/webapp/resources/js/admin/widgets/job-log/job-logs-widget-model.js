define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		defaults: {},

		initialize: function ( options ) {
			this.jobTaskId = options.options.jobTaskId;
		},

		url: function () {
			return '/admin/rest/jobs/' + this.jobTaskId + '/logs/';
		}
	} );
} );