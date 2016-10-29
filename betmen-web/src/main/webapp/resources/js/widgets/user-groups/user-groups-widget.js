define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var Model = require( './user-groups-widget-model' );
	var View = require( './user-groups-widget-view' );

	function init( container, options ) {

		var model = new Model.UserGroups( { options: options } );
		var view = new View( { model: model, el: container, options: options } );

		return {

			view: function() {
				return view;
			}
		}
	}

	return init;
} );
