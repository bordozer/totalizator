define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-model' );
	var View = require( './matches-and-bets-compact-vew' );

	function init( container, options ) {

		var model = new Model.MatchesModel( { options: options } );
		var view = new View( { model: model, el: container, options: options } );

		return {

			view: function () {
				return view;
			}
		}
	}

	return init;
} );