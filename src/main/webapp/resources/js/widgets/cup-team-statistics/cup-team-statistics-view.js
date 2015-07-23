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
		, teamPlayedMatches: "Team played matches"
		, teamWonMatches: "Team won matches"
		, menuOpenCupCard: 'Open cup card'
		, matches: 'matches'
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
				, {selector: 'divider'}
				, {
					selector: 'js-menu-team1-matches',
					icon: 'fa fa-futbol-o',
					link: '/totalizator/cups/15/matches/teams/' + this.model.team.teamId + '/',
					text: this.model.team.teamName + ' - ' + translator.matches + ' ( ' + this.model.cup.cupName + ' )'
				}
			];
		},

		_renderCupStatistics: function () {

			var data = _.extend( {}, this.model.toJSON(), { team: this.model.team, translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		}
	});
} );

