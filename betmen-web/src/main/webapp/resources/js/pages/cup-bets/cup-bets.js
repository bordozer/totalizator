define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './cup-bets-model' );
	var View = require( './cup-bets-view' );

	function init( container, options ) {

		var model = new Model.CupPageModel( { options: options } );
		var view = new View.CupPageView( { model: model, el: container, options: options } );

		return {

			view: function() {
				return view;
			}
		}
	}

	return init;
} );
