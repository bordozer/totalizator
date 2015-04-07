define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-bets-template.html' ) );

	var CupsNaviView = require( 'js/components/cups-navi/cups-navi' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Match best title"
		, userLabel: "User"
		, userPointsLabel: "Points"
	} );

	var MatchBetsView = Backbone.View.extend( {

		initialize: function ( options ) {
			this.matchId = options.options.matchId;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			var match = this.model.toJSON().match;
			var matchResults = service.matchResultsByMatch( match );

			var data = _.extend( {}, this.model.toJSON(), { matchResults: matchResults, currentUser: this.currentUser, translator: translator } );

			this.$el.html( template( data ) );

			this._renderNavigation();

			return this;
		},

		_renderNavigation: function() {
			var selectedCupId = 0;
			var cupsNaviView = new CupsNaviView( selectedCupId, this.$( '.js-cups-navi' ) );
		}
	} );

	return { MatchBetsView: MatchBetsView };
} );
