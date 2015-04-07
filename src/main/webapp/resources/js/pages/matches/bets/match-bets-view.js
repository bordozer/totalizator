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
		, userLabel: "User"
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
			var winnerId = match.score1 > match.score2 ? match.team1Id : match.score1 < match.score2 ? match.team2Id : 0;

			var style1 = winnerId == match.team1Id ? 'text-info' : winnerId == match.team2Id ? 'text-muted' : '';
			var style2 = winnerId == match.team2Id ? 'text-info' : winnerId == match.team1Id ? 'text-muted' : '';

			var data = _.extend( {}, this.model.toJSON(), { style1: style1, style2: style2, translator: translator } );
			console.log( data );

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
