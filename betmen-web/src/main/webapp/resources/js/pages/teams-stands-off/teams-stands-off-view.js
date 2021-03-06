define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var templateTeams = _.template( require( 'text!./templates/teams-template.html' ) );
	var templateHistory = _.template( require( 'text!./templates/teams-stands-off-template.html' ) );
	var templateNoHistory = _.template( require( 'text!./templates/no-history.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var app = require( 'app' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Teams standoff history"
		, teamsHaveNoMatchesYet: "The teams have no matches yet"
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-show-cup': '_showCupStandoff'
		},

		initialize: function ( options ) {
			this.team1Id = options.options.team1Id;
			this.team2Id = options.options.team2Id;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			var model = this.model.toJSON();

			var teamsData = _.extend( {}, {
				team1: model.team1
				, team2: model.team2
				, translator: translator
			} );
			this.$el.html( templateTeams( teamsData ) );

			if ( ! model.standoffsByCup ) {
				this.$el.append( templateNoHistory( { translator: translator } ) );
				return;
			}

			_.each( model.standoffsByCup, function( standoff ) {
				standoff.style1 = standoff.score1 > standoff.score2 ? 'text-success' : ( standoff.score1 < standoff.score2 ? 'text-danger' : '' );
				standoff.style2 = standoff.score2 > standoff.score1 ? 'text-success' : ( standoff.score2 < standoff.score1 ? 'text-danger' : '' );
			});

			var data = _.extend( {}, {
				standoffsByCup: model.standoffsByCup
				, selectedCup: model.cupToShow
				, translator: translator
			} );
			this.$el.append( templateHistory( data ) );

			this._renderMatches( model.cupToShow );
		},

		_renderMatches: function( cup ) {

			var model = this.model.toJSON();

			var el = this.$( '.js-teams-stands-off-matches' );

			var options = {
				filter: {
					categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, teamId: model.team1.teamId
					, team2Id: model.team2.teamId
					, showFinished: true
					, showFutureMatches: true
					, sorting: 2
				}
				, matchViewMode: 2
				, currentUser: app.currentUser()
			};

			matchesAndBetsView( el, options );
		},

		_showCupStandoff: function( evt ) {

			var cupId = $( evt.target ).data( 'cupid' );

			this.$( '.js-cups' ).removeClass( 'alert-warning' );
			this.$( '.js-cup-' + cupId ).addClass( 'alert-warning' );

			this._renderMatches( service.loadPublicCup( cupId ) );
		}
	});
} );