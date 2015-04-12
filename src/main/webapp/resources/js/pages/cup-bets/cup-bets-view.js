define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-bets-template.html' ) );

	var MatchesModel = require( 'js/components/matches-and-bets/matches-and-bets-model' );
	var MatchesView = require( 'js/components/matches-and-bets/matches-and-bets-view' );

	var CupScoresModel = require( 'js/components/cup-users-scores/cup-users-scores-model' );
	var CupScoresView = require( 'js/components/cup-users-scores/cup-users-scores-view' );

	var cupTeamBets = require( 'js/components/cup-team-bets/cup-team-bets' );

	var CupPageView = Backbone.View.extend( {

		initialize: function( options ) {
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
			 } ) );

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this._getCup().cupId } );

			this._renderCupScores();

			this._renderCupMatchesAndBets();

			this._renderCupTeamBets();

			return this;
		},

		_renderCupScores: function() {
			var el = this.$( '.js-cup-scores' );

			var cup = this._getCup();

			var model = new CupScoresModel( { cupId: cup.cupId } );

			var view = new CupScoresView( {
				model: model
				, el: el
			} );
		},

		_renderCupMatchesAndBets: function() {

			var currentUser = this.currentUser;

			var el = this.$( '.js-cup-matches-and-bets' );

			var container = $( '<div></div>' );
			el.append( container );

			var cup = this._getCup();

			var model = new MatchesModel.MatchesModel();

			var view = new MatchesView.MatchesView( {
				model: model
				, el: container
				, settings: {
					userId: 0
					, categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, teamId: 0
				}
				, menuItems: []
				, currentUser: currentUser
			} );
		},

		_renderCupTeamBets: function() {

			var el = this.$( '.js-cup-team-bets' );

			cupTeamBets( el, { cup: this._getCup(), currentUser: this.currentUser } );
		},

		_getCup: function() {
			return this.model.toJSON();
		}
	} );

	return { CupPageView: CupPageView };
} );

