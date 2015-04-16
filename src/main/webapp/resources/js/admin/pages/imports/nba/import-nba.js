define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './import-nba-model' );
	var View = require( './import-nba-view' );

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

	// Game info
	// http://stats.nba.com/stats/boxscore?GameID=0021300592&RangeType=0&StartPeriod=0&EndPeriod=0&StartRange=0&EndRange=0
	// http://stats.nba.com/stats/boxscore?GameID=0021401230&RangeType=0&StartPeriod=0&EndPeriod=0&StartRange=0&EndRange=0