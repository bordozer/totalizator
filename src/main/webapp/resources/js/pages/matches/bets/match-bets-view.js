define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bets-template.html' ) );
	var templateEntry = _.template( require( 'text!./templates/match-bets-entry-template.html' ) );

	var CupsNaviView = require( 'js/components/cups-navi/cups-navi' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Match best title"
	} );

	var MatchBetsView = Backbone.View.extend( {

		initialize: function ( options ) {
			this.matchId = options.options.matchId;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			this._renderNavigation();

			this._renderMatchBets();

			return this;
		},

		_renderNavigation: function() {
			var selectedCupId = 0;
			var cupsNaviView = new CupsNaviView( selectedCupId, this.$( '.js-cups-navi' ) );
		},

		_renderMatchBets: function() {

			var currentUser = this.currentUser;

			var container = this.$( '.js-match-bets-container' );
			console.log( container );

			container.empty();

			_.each( this.model.get( 'matchBets' ), function( matchBet ) {
				var bet = matchBet.bet;
				var data = _.extend( {}, bet, { currentUser: currentUser, translator: translator } );
				console.log( data );
				container.append( templateEntry( data ) );
			} );
		}
	} );

	return { MatchBetsView: MatchBetsView };
} );
