define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './cups-navi-model' );
	var View = require( './cups-navi-view' );

	function init( selectedCupId, container ) {

		var model = new Model();
		var view = new View( { model: model, el: container, selectedCupId: selectedCupId } );

		return {

			view: function() {
				return view;
			}
		}
	}

	return init;
} );
