define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-card-template.html' ) );
	var templateNoActivity = _.template( require( 'text!./templates/user-card-no-activity-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );
	//var userStatisticsWidget = require( 'js/widgets/user-statistics/user-statistics-widget' );
	var activityStreamWidget = require( 'js/widgets/activity-stream/activity-stream-widget' );

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

			this._renderActivityStream();

			this._renderUserBetsFuture();

			this._renderUserBetsPast();
		},

		_renderUserBetsFuture: function() {

			var userId = this.userId;
			var currentUser = this.currentUser;
			var cupsToShow = this.model.get( 'cupsToShow' );

			var container = this.$( '.js-user-bets-future' );

			_.each( cupsToShow, function( cup ) {

				var el = $( '<div></div>' );
				container.append( el );

				var options = {
					filter: {
						userId: userId
						, categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFinished: false
						, showFutureMatches: true
						, sorting: 2
					}
					, matchViewMode: 3
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			});
		},

		_renderUserBetsPast: function() {

			var userId = this.userId;
			var currentUser = this.currentUser;
			var cupsToShow = this.model.get( 'cupsToShow' );

			var container = this.$( '.js-user-bets-past' );

			if ( cupsToShow.length == 0 ) {
				this.$( '.js-user-bets-future' ).html( templateNoActivity( { translator: translator } ) );
				return;
			}

			if ( this.filterByCupId ) {

				var el = $( '<div></div>' );
				container.append( el );

				var cup = service.loadPublicCup( this.filterByCupId );

				var options = {
					filter: {
						userId: userId
						, categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFinished: true
						, showFutureMatches: false
						, sorting: 2
					}
					, matchViewMode: 3
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			}

		},

		_renderActivityStream: function () {
			activityStreamWidget( this.$( '.js-user-activity-stream' ), { userId: this.userId } );
		}
	});
} );
