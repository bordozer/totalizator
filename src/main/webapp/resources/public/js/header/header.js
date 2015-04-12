define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './header-model' );
	var View = require( './header-view' );

	function init( container, options ) {

		var model = new Model();
		var view = new View.HeaderView( { model: model, el: container, options: options } );

		return {

			view: function() {
				return view;
			}
		}
	}

	return init;
});
