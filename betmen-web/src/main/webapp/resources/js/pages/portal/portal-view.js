define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/portal-template.html' ) );
	var templateDateDependent = _.template( require( 'text!./templates/portal-date-dependant-part-template.html' ) );
	var templateNoMatchesOnDate = _.template( require( 'text!./templates/no-games-on-date.html' ) );
	var templateAnotherMatches = _.template( require( 'text!./templates/portal-another-matches-template.html' ) );
	var templateNoFavoriteCategories = _.template( require( 'text!./templates/portal-no-favorite-categories.template.html' ) );

	var matchesAndBetsView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );
	var activityStreamWidget = require( 'js/widgets/activity-stream/activity-stream-widget' );
	var usersRatingWidget = require( 'js/widgets/users-rating/users-rating-widget' );
	var favoritesCategoriesStatisticsWidget = require( 'js/widgets/fav-cat-stat/fav-cat-stat-widget' );
	var DefineFavoriteCategoriesView = require( 'js/components/favorites/define-favorite-categories-view' );

	var app = require( 'app' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var DateChooser = require( 'js/controls/date-chooser/date-chooser-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noMatchesOnDateLabel: 'No matches of favorite leagues on date'
		, noMatchesOnDatePossibleReasonsLabel: 'No matches on date possible reasons'
		, noGamesOnDateLabel: 'No games on date'
		, noFavoritesCategoriesLabel: 'No favorite leagues'
		, noBetsOnDateLabel: 'No your bets on date'
		, todayLabel: 'Today'
		, matchesOfNonFavoriteCategoriesOnDateLabel: 'Another matches (not favorite categories)'
		, matchesOnDateLabel: 'Matches on date'
		, noFavoriteCategoriesLabel: 'You do not have favorite categories track to'
		, goAndCreateFavoriteCategoriesLabel: 'Define favorite categories'
		, favoriteCategoriesDefinedLabel: 'Favorite categories defined'
	} );

	var VIEW_MODE_BET = 1;
	var VIEW_MODE_TABLE = 2;
	var VIEW_MODE_MINIMIZED = 3;

	var MATCHES_AND_BETS_MODE_MATCHES = 1;
	var MATCHES_AND_BETS_MODE_STATISTICS = 2;

	return Backbone.View.extend( {

		events: {
			'click .js-favorite-categories-defined': '_favoriteCategoriesDefined'
		},

		initialize: function( options ) {

			this.onDate = options.options.onDate;

			this.model.on( 'sync', this._renderDateDependant, this );

			this.render();
		},

		render: function () {

			this.$el.html( template() );

			this.$( '.js-date-dependent-part' ).html( templateDateDependent( {
				translator: translator
			} ) );

			this._renderDatesChooser();

			this._renderActivityStream();
		},

		_renderDatesChooser: function () {

			var dateChooser = new DateChooser( {
				el: this.$( '.js-date-chooser' )
				, options: { onDate: this.onDate, todayUrl: /betmen/ }
			} );

			dateChooser.on( 'events:change_date', this._loadDataForDate, this  );

			dateChooser.render();
		},

		_renderDateDependant: function () {

			var onDate = this.onDate;

			this._renderMatchesOnDate( onDate );

			this._renderAnotherMatchesOnDate(onDate);

			this._renderCupStatistics( onDate );

			this._renderFavoritesCategoriesStatisticsWidget(onDate);

			this._renderTodayUserRating( onDate );

			this._renderYesterdayUserRating( onDate );

			this._renderMonthUserRating( onDate );
		},

		_renderMatchesOnDate: function ( onDate ) {

			var model = this.model.toJSON();
			var currentUser = app.currentUser();

			var container = this.$( '.js-portal-page-favorite-categories-matches-on-date' );
			container.empty();

			if ( model.cupsTodayToShow.length == 0 ) {
				container.html(templateNoMatchesOnDate({translator: translator}));
				return;
			}

			_.each( model.cupsTodayToShow, function( cup ) {

				var el = $( '<div></div>' );
				container.append( el );

				var options = {
					filter: {
						categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFutureMatches: true
						, showFinished: true
						, filterByDateEnable: true
						, filterByDate: onDate
						, sorting: 1
					}
					, matchViewMode: VIEW_MODE_MINIMIZED
					, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_MATCHES
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			} );
		},

		_renderAnotherMatchesOnDate: function(onDate) {
			var model = this.model.toJSON();
			var container = this.$( '.js-portal-page-another-matches-on-date' );
			container.empty();
			if (model.anotherMatchesOnDate.length == 0) {
				return;
			}
			container.html(templateAnotherMatches({
				anotherMatches: model.anotherMatchesOnDate
				, onDate: onDate
				, translator: translator
			}));
		},

		_renderCupStatistics: function( onDate ) {

			var model = this.model.toJSON();

			var currentUser = app.currentUser();

			var container = this.$( '.js-portal-page-container' );
			container.empty();

			var row = $( "<div class='row'></div>" );
			container.append( row );

			var hasFavoriteCategories = model.cupsToShow.length > 0;
			if (!hasFavoriteCategories) {
				row.html(templateNoFavoriteCategories({translator: translator}));
				new DefineFavoriteCategoriesView({el: $('.js-define-favorite-categories', row)});
				return;
			}

			_.each( model.cupsToShow, function( cup ) {

				var el = $( '<div class="col-xs-12"></div>' );
				row.append( el );

				var options = {
					filter: {
						categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFutureMatches: true
						, filterByDate: onDate
						, filterByDateEnable: true
						, sorting: 1
					}
					, matchViewMode: VIEW_MODE_BET
					, matchesAndBetsViewMode: MATCHES_AND_BETS_MODE_STATISTICS
					, currentUser: currentUser
				};
				matchesAndBetsView( el, options );
			} );
		},

		_renderFavoritesCategoriesStatisticsWidget: function (onDate) {
			var nestDate = dateTimeService.formatDate(dateTimeService.plusDay(onDate));
			favoritesCategoriesStatisticsWidget(this.$('.js-favorites-categories-statistics'), {onDate: nestDate});
		},

		_renderTodayUserRating: function( onDate ) {
			usersRatingWidget( this.$( '.js-users-rating-today' ), { onDate: onDate, onDateTo: onDate } );
		},

		_renderYesterdayUserRating: function( onDate ) {
			var prevDate = dateTimeService.formatDate( dateTimeService.minusDay( onDate ) );
			usersRatingWidget( this.$( '.js-users-rating-yesterday' ), { onDate: prevDate, onDateTo: prevDate } );
		},

		_renderMonthUserRating: function(onDate) {
			var mm = dateTimeService.toMomentDate(onDate);

			var onDateFrom = dateTimeService.formatDate(dateTimeService.startOfMonth(mm));
			var onDateTo = dateTimeService.formatDate(dateTimeService.endOfMonth(mm));

			usersRatingWidget( this.$( '.js-users-rating-month' ), { onDate: onDateFrom, onDateTo: onDateTo } );
		},

		_renderActivityStream: function () {
			activityStreamWidget( this.$( '.js-portal-page-activity-stream' ), {} );
		},

		_loadDataForDate: function( onDate ) {
			this.onDate = onDate;
			this.spinning();
			this.model.refresh( onDate );
		},

		spinning: function () {

			var div = "<div class='text-center text-muted'><i class='fa fa-spinner fa-spin fa-5x'></i></div>";

			this.$( '.js-portal-page-favorite-categories-matches-on-date' ).html( div );
			this.$( '.js-portal-page-another-matches-on-date' ).html( div );
			this.$( '.js-portal-page-container' ).html( div );
			this.$( '.js-users-rating-today' ).html( div );
			this.$( '.js-users-rating-yesterday' ).html( div );
			this.$( '.js-users-rating-month' ).html( div );
		},

		_favoriteCategoriesDefined: function () {
			window.location.reload();
		}
	} );
} );

