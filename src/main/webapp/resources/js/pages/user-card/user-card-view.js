define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-card-template.html' ) );
	var templateNoActivity = _.template( require( 'text!./templates/user-card-no-activity-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );
	var userStatisticsWidget = require( 'js/widgets/user-statistics/user-statistics-widget' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userStatisticsLabel: "User card: Statistics"
		, userBetsLabel: "User card: User bets"
		, noUserBetsLabel: "The user has not made bets"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.userId = options.options.userId;
			this.currentUser = options.options.currentUser;
			this.filterByCupId = options.options.filterByCupId;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			} ) );

			this._renderUserStatistics();

			this._renderUserBets();
		},

		_renderUserBets: function() {

			var userId = this.userId;
			var currentUser = this.currentUser;

			var el = this.$( '.js-user-bets' );

			var cupsToShow = this.model.get( 'cupsToShow' );

			if ( cupsToShow.length == 0 ) {
				this.$( '.js-user-bets' ).html( templateNoActivity( { translator: translator } ) );
				return;
			}

			if ( this.filterByCupId ) {

				var container = $( '<div class="col-xs-12"></div>' );
				el.append( container );

				var cup = service.loadPublicCup( this.filterByCupId );

				var options = {
					filter: {
						userId: userId
						, categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFinished: true
						, showFutureMatches: false
					}
					, viewMode: 2
					, currentUser: currentUser
				};
				matchesAndBetsView( container, options );
			}

			_.each( cupsToShow, function( cup ) {

				var container = $( '<div class="col-xs-12"></div>' );
				el.append( container );

				var options = {
					filter: {
						userId: userId
						, categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFinished: false
						, showFutureMatches: true
					}
					, viewMode: 2
					, currentUser: currentUser
				};
				matchesAndBetsView( container, options );
			});
		},

		_renderUserStatistics: function() {

			var categoryId = 1;		// TODO
			var cupId = 1;			// TODO

			var options = {
				filter: {
					categoryId: categoryId
					, cupId: cupId
					, showFinished: true
					, userId: this.userId
				}
				, viewMode: 1
				, menuItems: []
			};

			userStatisticsWidget( this.$( '.js-user-statistics' ), options );
		}
	});
} );
