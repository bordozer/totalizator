define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bets-template.html' ) );

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
			this.$el.html( template( {
				translator: translator
			} ) );

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
		}
	} );

	return { MatchBetsView: MatchBetsView };
} );
