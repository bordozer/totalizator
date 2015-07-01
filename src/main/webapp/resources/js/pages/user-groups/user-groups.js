define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var Model = require( './user-groups-model' );
	var View = require( './user-groups-view' );

	function init( container, options ) {

		var model = new Model( { options: options } );
		var view = new View( { model: model, el: container, options: options } );

		return {

			view: function() {
				return view;
			}
		}
	}

	return init;
} );