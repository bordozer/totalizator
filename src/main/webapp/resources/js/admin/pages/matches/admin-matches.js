define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-matches-model' );
	var View = require( './admin-matches-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		addMatchLabel: "Admin / Matches / Add entry"
		, finishSelectedMatchesLabel: "Admin / Matches / Finish selected matches"
	} );

	function init( container, options ) {

		var matchesModel = new Model.MatchesModel( { options: options } );

		var menuItems = [
			{ selector: 'divider' }
			, { selector: 'js-add-entry-button', icon: 'fa fa-plus', link: '#', text: translator.addMatchLabel }
			, { selector: 'js-finish-selected-matches-button', icon: 'fa fa-flag-checkered', link: '#', text: translator.finishSelectedMatchesLabel }
		];

		var matchesView = new View.MatchesView( {
			model: matchesModel
			, el: container
			, settings: {
				userId: 0
				, categoryId: options.categoryId
				, cupId: options.cupId
				, teamId: 0
			}
			, menuItems: menuItems
		} );

		return {
			view: function() {
				return matchesView;
			}
		}
	}

	return init;
});
