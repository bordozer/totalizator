define( function ( require ) {

	'use strict';

	var Model = require( './match-bets-model' );
	var View = require( './match-bets-view' );

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
