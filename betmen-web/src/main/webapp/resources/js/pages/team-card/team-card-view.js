define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var app = require( 'app' );

	var template = _.template( require( 'text!./templates/team-card-template.html' ) );

	var CupTeamStatistics = require( 'js/widgets/cup-team-statistics/cup-team-statistics' );
	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noCupLabel: "Team has not been a participant of any cup"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON() );
			this.$el.html( template( data ) );

			this._renderCupsStatistics();
			this._renderTeamGames();
		},

		_renderCupsStatistics: function() {

			var model = this.model.toJSON();

			var container = this.$( '.js-cups-statistics' );

			if ( model.cardCupData.length == 0 ) {
				container.html( translator.noCupLabel );
				return;
			}

			_.each( model.cardCupData, function( cupData ) {

				var cup = cupData.cup;

				var el = $( '<div class="col-xs-4"></div>' );
				container.append( el );

				new CupTeamStatistics( el, { team: model.team, cup: cup } );
			});
		},

		_renderTeamGames: function() {
			var currentUser = app.currentUser();
			var model = this.model.toJSON();
			var teamId = model.team.teamId;
			var self = this;
			var container  = $('.js-team-games');
			var cups = service.loadAllTeamActivePublicCups(teamId);

			_.each( cups, function( cupAndCategoryPair ) {
				var el = $( '<div class="col-xs-12"></div>' );
				container.append( el );

				var options = {
					filter: {
						userId: currentUser.userId
						, categoryId: cupAndCategoryPair.categoryId
						, cupId: cupAndCategoryPair.cupId
						, teamId: model.team.teamId
						, filterByDateEnable: false
						, showFinished: true
						, showFutureMatches: false
						, sorting: 2
					}
					, matchViewMode: 3
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			});
		}
	});
} );

