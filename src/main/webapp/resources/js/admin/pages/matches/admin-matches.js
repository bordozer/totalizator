define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Model = require( './admin-matches-model' );
	var View = require( './admin-matches-view' );

	var FilterModel = require( 'js/components/widget-configurable/filter/matches-filter-model' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		addMatchLabel: "Admin / Matches / Add entry"
		, finishSelectedMatchesLabel: "Admin / Matches / Finish selected matches"
		, deleteSelectedMatchesLabel: "Admin / Matches / Delete selected matches"
	} );

	function init( container, options ) {

		var matchesModel = new Model.MatchesModel( { options: options } );

		var menuItems = [
			{ selector: 'divider' }
			, { selector: 'js-add-entry-button', icon: 'fa fa-plus', link: '#', text: translator.addMatchLabel, button: true }
			, { selector: 'divider' }
			, { selector: 'js-finish-selected-matches-button', icon: 'fa fa-flag-checkered', link: '#', text: translator.finishSelectedMatchesLabel }
			, { selector: 'js-delete-selected-matches-button', icon: 'fa fa-close', link: '#', text: translator.deleteSelectedMatchesLabel }
		];

		var filter = {
			categoryId: options.categoryId
				, cupId: options.cupId
				, showFutureMatches: true
		};
		var filterModel = new FilterModel( filter );

		var matchesView = new View.MatchesView( {
			model: matchesModel
			, el: container
			, filterModel: filterModel
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
