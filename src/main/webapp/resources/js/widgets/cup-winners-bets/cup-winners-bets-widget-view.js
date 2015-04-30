define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-winners-bets-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "All cup winners bets"
		, userLabel: 'User'
		, cupPositionLabel: 'cup position'
		, realCupWinnersLabel: 'Cup winners'
		, noWinnerBetLabel: 'No bet'
		, teamIsOutOfCupLabel: 'Team has been knocked out'
	} );

	return WidgetView.extend( {

		initialize: function ( options ) {
			this.cupId = options.options.cupId;

			this.listenTo( this.model, 'sync', this._renderCupWinnersBets );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			var cup = service.getCup( service.loadPublicCups(), this.cupId );
			return this.getCupTitle( cup, translator.title );
		},

		getIcon: function () {
			return 'fa-money';
		},

		_renderCupWinnersBets: function () {

			var model = this.model.toJSON();

			var data = _.extend( {}, model, { translator: translator } );

			this.setBody(  template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		}
	} );
} );