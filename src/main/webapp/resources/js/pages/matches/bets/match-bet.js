define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './match-bet-model' );
	var View = require( './match-bet-view' );

	function init( container, options ) {

		var model = new Model.MatchBetsModel( { options: options } );
		var view = new View.MatchBetsView( { model: model, el: container, options: options } );

		return {
			view: function() {
				return view;
			}
		}
	}

	return init;
} );
