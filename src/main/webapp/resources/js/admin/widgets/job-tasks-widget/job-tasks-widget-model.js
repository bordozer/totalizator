define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var JobTaskIdModel = Backbone.Model.extend( {

		idAttribute: 'jobTaskId',

		defaults: {
			id: 0
		}
	});

	return Backbone.Collection.extend( {

		model: JobTaskIdModel,

		initialize: function ( options ) {

		},

		url: function () {
			return '/admin/rest/jobs/';
		}
	} );
} );