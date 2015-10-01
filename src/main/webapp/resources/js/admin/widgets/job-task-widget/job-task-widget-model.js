define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		idAttribute: 'jobTaskId',

		defaults: {
			jobTaskId: 0
			, jobTaskName: 'New job task'
			, jobTaskTypeId: 0
			, jobTaskActive: true
			, jobTaskParametersHolder: {}
			, jobTaskExecuting: false
		},

		initialize: function ( options ) {
			this.set( { jobTaskId: options.options.jobTaskId } );
			this.jobTaskTypeId = options.options.jobTaskTypeId;
		},

		url: function () {
			return '/admin/rest/jobs/' + this.jobTaskTypeId + '/' + this.get( 'jobTaskId' ) + '/';
		}
	} );
} );