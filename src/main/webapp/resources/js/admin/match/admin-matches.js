define( function( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var ConfigurableView = require( 'js/components/configurable-view/configurable-view' );

	var Model = require( './admin-matches-model' );
	var View = require( './admin-matches-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		addMatchLabel: "Admin / Matches / Add entry"
	} );

	function init( container ) {

		var matchesModel = new Model.MatchesModel();
		var matchesView = new View.MatchesView( {
			model: matchesModel
		} );

		var menuItems = [
			{ selector: 'js-add-entry-button', icon: 'fa fa-plus', link: '#', text: translator.addMatchLabel }
			, { selector: 'divider' }
		];

		var configurableView = new ConfigurableView( {
			el: container
			, view: matchesView
			, settings: {
				categoryId: 0
				, cupId: 0
			}
			, menuItems: menuItems
		} );

		return {

			view: function() {
				return configurableView;
			}
		}
	}

	return init;
});
