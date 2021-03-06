define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './job-logs-widget-model' );
	var View = require( './job-logs-widget-view' );

	function init( container, options ) {

		var model = new Model( { options: options } );
		var view = new View( { model: model, el: container, options: options } );

		return {
			view: function () {
				return view;
			}
		}
	}

	return init;
} );