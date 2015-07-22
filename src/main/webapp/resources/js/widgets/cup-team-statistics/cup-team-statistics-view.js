define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-team-statistics-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Team card"
		, cupPosition: "cup position"
		, teamWonMatches: "Team won matches"
		, menuOpenCupCard: 'Open cup card'
	} );

	return WidgetView.extend( {

		initialize: function ( options ) {
			this.listenTo( this.model, 'sync', this._renderCupStatistics );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.getCupTitle( this.model.cup, translator.title );
		},

		getIcon: function () {
			return 'fa-gift';
		},

		getPictureURL: function() {
			return this.model.cup.logoUrl;
		},

		getCustomMenuItems: function() {
			return [
				{ selector: 'js-menu-cup-card'
					, icon: 'fa fa-external-link'
					, link: '/totalizator/cups/' + this.model.cup.cupId + '/'
					, text: translator.menuOpenCupCard
				}
			];
		},

		_renderCupStatistics: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		}
	});
} );

