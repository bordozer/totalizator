define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/team-matches-template.html' ) );

	var matchesAndBetsWidget = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var app = require( 'app' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {},

		initialize: function ( options ) {

			this.cup = service.loadPublicCup( options.options.cupId );
			this.teamId = options.options.teamId;

			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			this._renderTeamMatches();
		},

		_renderTeamMatches: function() {

			var options = {
				filter: {
					categoryId: this.cup.category.categoryId
					, cupId: this.cup.cupId
					, teamId: this.teamId
					, showFutureMatches: true
					, showFinished: true
					, sorting: 2
				}
				, matchViewMode: 2
				, currentUser: app.currentUser()
			};

			matchesAndBetsWidget( this.$( '.js-team-matches' ), options );
		}
	} );
} );