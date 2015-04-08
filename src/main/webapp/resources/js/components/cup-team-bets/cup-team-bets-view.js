define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-team-bets-template.html' ) );
	var templateEdit = _.template( require( 'text!./templates/cup-team-bets-template-edit.html' ) );

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
			this.teams = service.loadTeams();

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		renderBody: function () {


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

	var CupTeamBetsDetails = Backbone.View.extend({

		initialize: function( options ) {

		},

		render: function () {

		}
	});

	var CupTeamBetsEdit = Backbone.View.extend({

		initialize: function( options ) {
			this.teams = service.loadTeams();
		},

		render: function () {
			var model = this.model.toJSON();

			var data = _.extend( {}, model, { cup: this.cup, teams: this.teams, translator: translator } );

			this.setBody( templateEdit( data ) );

			var self = this;
			_.each( model.cupTeamBets, function( cupTeamBet ) {
				var cupPosition = cupTeamBet.cupPosition;
				self.$( '#cup-team-position-' + cupPosition.cupPositionId  ).chosen( { width: '100%' } );
			});

			return this;
		}
	});
} );
