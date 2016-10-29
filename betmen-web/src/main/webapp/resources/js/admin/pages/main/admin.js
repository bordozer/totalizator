define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-model' );
	var View = require( './admin-view' );

	function init( container, options ) {

		var model = new Model.AdminModel( { options: options } );
		var view = new View.AdminView( { model: model, el: container, options: options } );

		return {

			model: function () {
				return model;
			},

			view: function () {
				return view;
			}
		}
	}

	return init;
} );
