define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-statistics-widget-template.html' ) );

	var WidgetMatchesAndBets = require( 'js/components/widget-matches-and-bets/widget-matches-and-bets' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		label: 'User statistics'
		, underConstructionLabel: 'Under construction'
	} );

	return WidgetMatchesAndBets.extend( {

		renderInnerView: function( filter ) {
			this.listenToOnce( this.model, 'sync', this._renderUSerStatistics );
			//this.model.refresh( filter ); // TODO: enable to get data
			this._renderUSerStatistics();
		},

		getTitle: function () {
			return translator.label;
		},

		getIcon: function () {
			return 'fa-gift';
		},

		_renderUSerStatistics: function() {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			var el = this.$( this.windowBodyContainerSelector );
			el.empty();

			el.html( template( data ) );

			this.trigger( 'inner-view-rendered' );
		}
	})
} );
