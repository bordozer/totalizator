define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var template = _.template( require( 'text!./templates/teams-stands-off-template.html' ) );

	var MatchesModel = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-model' );
	var MatchesAndBetsCompactView = require( 'js/widgets/matches-and-bets-compact/matches-and-bets-compact-widget-vew' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams standoff history"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cups = options.options.cups;
			this.team1 = options.options.team1;
			this.team2 = options.options.team2;

			this.score1 = options.options.score1;
			this.score2 = options.options.score2;

			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {
			var data = _.extend( {}, { team1: this.team1, team2: this.team2, score1: this.score1, score2: this.score2, translator: translator } );
			this.$el.html( template( data ) );

			this._renderMatches();
		},

		_renderMatches: function() {

			var self = this;

			var el = this.$( '.js-teams-stands-off-matches' );

			var currentUser = this.currentUser;

			var cupsToShow = this.model.get( 'cupsToShow' );

			_.each( cupsToShow, function( cup ) {

				var container = $( '<div class="col-lg-12"></div>' );
				el.append( container );

				var model = new MatchesModel.MatchesModel();

				var view = new MatchesAndBetsCompactView( {
					model: model
					, el: container
					, settings: {
						userId: 0
						, categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, teamId: self.team1.teamId
						, team2Id: self.team2.teamId
						, showFinished: true
						, showFutureMatches: false
					}
					, menuItems: []
					, currentUser: currentUser
				} );
			});
		}
	});
} );