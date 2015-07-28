define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-matches-template.html' ) );

	var matchesAndBetsWidget = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup matches"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {

			this.cupId = options.options.cupId;
			this.team1Id = options.options.team1Id;
			this.team2Id = options.options.team2Id;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } );

			var cup = this.model.toJSON();

			var data = _.extend( {}, { cup: cup, translator: translator } );

			this.$el.html( template( data ) );

			this._renderCupMatches( cup, true, '.js-cup-matches-future' );

			this._renderCupMatches( cup, false, '.js-cup-matches-finished'  );

			return this;
		},

		_renderCupMatches: function( cup, isFuture, selector ) {

			var options = {
				filter: {
					categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, teamId: this.team1Id
					, team2Id: this.team2Id
					, showFutureMatches: isFuture
					, showFinished: ! isFuture
				}
				, isCompactView: true
				, currentUser: this.currentUser
			};

			matchesAndBetsWidget( this.$( selector ), options );
		}
	} );
} );