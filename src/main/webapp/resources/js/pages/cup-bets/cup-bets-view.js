define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-bets-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var cupTeamBets = require( 'js/widgets/cup-bets/cup-bets-widget' );

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

			this._renderCupMatchesAndBets();

			this._renderCupBets();

			return this;
		},

		_renderCupMatchesAndBets: function() {

			var currentUser = this.currentUser;

			var el = this.$( '.js-cup-matches-and-bets' );

			var container = $( '<div></div>' );
			el.append( container );

			var cup = this._getCup();

			var options = {
				filter: {
					categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, showFutureMatches: true
				}
				, viewMode: 1
				, menuItems: []
				, currentUser: currentUser
			};
			matchesAndBetsView( container, options );
		},

		_renderCupBets: function() {

			var el = this.$( '.js-cup-bets' );

			cupTeamBets( el, { cup: this._getCup(), currentUser: this.currentUser } );
		},

		_getCup: function() {
			return this.model.toJSON();
		}
	} );

	return { CupPageView: CupPageView };
} );

