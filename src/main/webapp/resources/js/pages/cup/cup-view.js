define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-template.html' ) );

	var CupScoresModel = require( 'js/components/cup-users-scores/cup-users-scores-model' );
	var CupScoresView = require( 'js/components/cup-users-scores/cup-users-scores-view' );

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

			return this;
		},

		_renderCupScores: function( cup ) {

			var el = this.$( '.js-cup-scores' );

			var model = new CupScoresModel( { cup: cup } );

			var view = new CupScoresView( {
				model: model
				, el: el
			} );
		}
	} );
} );