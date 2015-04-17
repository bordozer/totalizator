define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var template = _.template( require( 'text!./templates/teams-stands-off-template.html' ) );

	var MatchesModel = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-model' );
	var MatchesView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams stand off"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cups = options.options.cups;
			this.team1 = options.options.team1;
			this.team2 = options.options.team2;

			this.render();
		},

		render: function() {
			var data = _.extend( {}, { team1: this.team1, team2: this.team2, translator: translator } );
			this.$el.html( template( data ) );

			this._renderMatches();
		},

		_renderMatches: function() {

			var currentUser = this.currentUser;

			var el = this.$( '.js-teams-stands-off-matches' );
			var self = this;

			_.each( this.cups, function( cup ) {

				var container = $( '<div class="col-lg-4"></div>' );
				el.append( container );

				var model = new MatchesModel.MatchesModel();
				var view = new MatchesView.MatchesView( {
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
			} );
		}
	});
} );