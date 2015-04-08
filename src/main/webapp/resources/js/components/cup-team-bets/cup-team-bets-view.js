define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-team-bets-template.html' ) );

	var WindowView = require( 'js/components/window/window-view' );
	var service = require( '/resources/js/services/service.js' );
	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup result bets"
	} );

	return WindowView.extend( {

		initialize: function( options ) {
			this.cup = options.options.cup;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		renderBody: function () {

			var model = this.model.toJSON();
			console.log( model );

			var teams = service.loadTeams();
			var data = _.extend( {}, model, { cup: this.cup, teams: teams, translator: translator } );

			this.setBody( template( data ) );

			var self = this;
			_.each( model.cupTeamBets, function( cupTeamBet ) {
				var cupPosition = cupTeamBet.cupPosition;
				self.$( '#cup-team-position-' + cupPosition.cupPositionId  ).chosen( { width: '100%' } );
			});

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-money';
		},

		getTitleHint: function() {
			return this.cup.category.categoryName + ': ' + this.cup.cupName;
		}
	});
} );
