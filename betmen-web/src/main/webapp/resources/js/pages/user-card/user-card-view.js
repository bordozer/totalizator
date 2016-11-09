define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-card-template.html' ) );
	var templateNoActivity = _.template( require( 'text!./templates/user-card-no-activity-template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );
	var activityStreamWidget = require( 'js/widgets/activity-stream/activity-stream-widget' );

	var DateChooser = require( 'js/controls/date-chooser/date-chooser-view' );

	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var app = require( 'app' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userStatisticsLabel: "User card: Statistics"
		, userBetsLabel: "User card: User bets"
		, noUserBetsLabel: "The user has not made bets at this date"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {

			this.userId = options.options.userId;
			this.filterByCupId = options.options.filterByCupId;
			this.onDate = options.options.onDate;

			this.model.on( 'sync', this._renderUserMatchesAndBets, this );

			this.render();
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			} ) );

			this._renderDatesChooser();

			this._renderActivityStream();
		},

		_renderDatesChooser: function () {

			var dateChooser = new DateChooser( {
				el: this.$( '.js-user-card-date-chooser' )
				, options: { onDate: this.onDate, todayUrl: '/betmen/users/' + this.userId + '/' }
			} );

			dateChooser.on( 'events:change_date', this._loadDataForDate, this  );

			dateChooser.render();
		},

		_loadDataForDate: function( onDate ) {
			this.onDate = onDate;
			this.model.fetch( { data: { onDate: onDate } }, { cache: false } );
		},

		_renderUserMatchesAndBets: function() {

			var userId = this.userId;
			var currentUser = app.currentUser();
			var cupsToShow = this.model.get( 'cupsToShow' );

			if ( cupsToShow.length == 0 ) {
				this.$( '.js-user-matches-and-bets' ).html( templateNoActivity( {
					onDate: dateTimeService.formatDateFullDisplay( this.onDate )
					, translator: translator
				} ) );
				return;
			}

			var container = this.$( '.js-user-matches-and-bets' );
			container.empty();

			var self = this;

			_.each( cupsToShow, function( cup ) {

				var el = $( '<div></div>' );
				container.append( el );

				var options = {
					filter: {
						userId: userId
						, categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, filterByDateEnable: true
						, filterByDate: self.onDate
						, showFinished: true
						, showFutureMatches: true
						, sorting: 1
					}
					, matchViewMode: 3
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			});
		},

		_renderActivityStream: function () {
			activityStreamWidget( this.$( '.js-user-activity-stream' ), { userId: this.userId } );
		}
	});
} );
