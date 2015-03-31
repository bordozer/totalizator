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
	} );

	function init( container ) {

		var matchesModel = new Model.MatchesModel();

		var menuItems = [
			{ selector: 'js-add-entry-button', icon: 'fa fa-plus', link: '#', text: translator.addMatchLabel }
			, { selector: 'divider' }
		];

		var matchesView = new View.MatchesView( {
			model: matchesModel
			, el: container
			, settings: {
				categoryId: 0
				, cupId: 0
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
