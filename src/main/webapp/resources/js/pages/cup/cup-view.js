define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-template.html' ) );

	var cupScores = require( 'js/components/cup-users-scores/cup-users-scores' );

	var cupTeams = require( 'js/widgets/cup-teams/cup-teams-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup overview"
		, cupBetsLabel: "Cup bets"
		, cupTeamsLabel: "Cup teams"
		, cupFinished: "Cup finished"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cupId = options.options.cupId;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } );

			var cup = this.model.toJSON();

			var data = _.extend( {}
					, {
						cup: cup
						, translator: translator
					}
			);

			this.$el.html( template( data ) );

			this._renderCupScores( cup );

			this._renderCupTeams( cup );

			return this;
		},

		_renderCupScores: function( cup ) {
			cupScores( this.$( '.js-cup-scores' ), { cup: cup } );
		},

		_renderCupTeams: function( cup ) {
			cupTeams( this.$( '.js-cup-teams' ), { cup: cup } );
		}
	} );
} );