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
		, teamFutureMatches: "Team future matches"
		, teamWonMatches: "Team won matches"
		, teamLostMatches: "Team lost matches"
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
			return this.getCupTitle( this.model.cup, '' );
		},

		getIcon: function () {
			return 'fa-gift';
		},

		getPictureURL: function() {
			return this.model.cup.logoUrl;
		},

		getCustomMenuItems: function() {
			return [
				{
					selector: 'js-menu-team1-matches',
					icon: 'fa fa-futbol-o',
					link: '/betmen/cups/' + this.model.cup.cupId + '/matches/teams/' + this.model.team.teamId + '/',
					text:  this.model.cup.cupName + ' / ' + this.model.team.teamName + ' - ' + translator.matches
				}
				, {selector: 'divider'}
				, { selector: 'js-menu-cup-card'
					, icon: 'fa fa-external-link'
					, link: '/betmen/cups/' + this.model.cup.cupId + '/'
					, text: translator.menuOpenCupCard
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

