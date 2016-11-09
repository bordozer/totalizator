define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var template = _.template( require( 'text!./templates/cup-matches-template.html' ) );

	var matchesAndBetsWidget = require( 'js/widgets/matches-and-bets/matches-and-bets-widget' );

	var DateChooser = require( 'js/controls/date-chooser/date-chooser-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup matches"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {

			this.cupId = options.options.cupId;
			this.team1Id = options.options.team1Id;
			this.team2Id = options.options.team2Id;
			this.currentUser = options.options.currentUser;
			this.onDate = options.options.onDate;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			var cup = this.model.toJSON();

			this.trigger( 'navigation:set:active:cup', { selectedCup: cup } );

			var data = _.extend( {}, { cup: cup, translator: translator } );

			this.$el.html( template( data ) );

			this._renderDatesChooser();

			this._renderFutureMatches( cup );

			return this;
		},

		_renderDatesChooser: function () {

			var dateChooser = new DateChooser( {
				el: this.$( '.js-cup-card-date-chooser' )
				, options: { onDate: this.onDate, todayUrl: '/betmen/cups/' + this.cupId + '/matches/' }
			} );

			dateChooser.on( 'events:change_date', this._loadDataForDate, this  );

			dateChooser.render();
		},

		_loadDataForDate: function( onDate ) {

			var cup = this.model.toJSON();

			this._renderPastMatchesOnDate( cup, onDate );
		},

		_renderPastMatchesOnDate: function( cup, onDate ) {

			var options = {
				filter: {
					categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, teamId: this.team1Id
					, team2Id: this.team2Id
					, filterByDateEnable: true
					, filterByDate: onDate
					, showFutureMatches: true
					, showFinished: true
					, sorting: 1
				}
				, matchViewMode: 3
				, currentUser: this.currentUser
			};

			matchesAndBetsWidget( this.$( '.js-user-past-matches-on-date' ), options );
		},

		_renderFutureMatches: function( cup ) {

			var options = {
				filter: {
					categoryId: cup.category.categoryId
					, cupId: cup.cupId
					, teamId: this.team1Id
					, team2Id: this.team2Id
					, showFutureMatches: true
					, showFinished: false
					, sorting: 1
				}
				, matchViewMode: 3
				, currentUser: this.currentUser
			};

			matchesAndBetsWidget( this.$( '.js-cup-matches-future' ), options );
		}
	} );
} );
