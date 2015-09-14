define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-template.html' ) );

	var cupScores = require( 'js/widgets/cup-users-scores/cup-users-scores-widget' );

	var cupTeams = require( 'js/widgets/cup-teams/cup-teams-widget' );

	var cupWinners = require( 'js/widgets/cup-winners/cup-winners-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup overview"
		, cupBetsLabel: "Cup bets"
		, cupMatchesLabel: "Cup matches"
		, allCupBetsLabel: "Cup result bets"
		, cupTeamsLabel: "Cup teams"
		, cupFinished: "Cup finished"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cupId = options.options.cupId;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			var cup = this.model.toJSON();

			this.trigger( 'navigation:set:active:cup', { selectedCup: cup } );

			var data = _.extend( {}, { cup: cup, translator: translator } );

			this.$el.html( template( data ) );

			this._renderCupScores( cup );

			this._renderCupTeams( cup );

			this._renderCupWinners( cup );

			return this;
		},

		_renderCupScores: function( cup ) {
			cupScores( this.$( '.js-cup-scores' ), { cup: cup, user: this.currentUser, currentUser: this.currentUser } );
		},

		_renderCupTeams: function( cup ) {
			cupTeams( this.$( '.js-cup-teams' ), { cup: cup } );
		},

		_renderCupWinners: function( cup ) {
			cupWinners( this.$( '.js-cup-winners' ), { cup: cup } );
		}
	} );
} );