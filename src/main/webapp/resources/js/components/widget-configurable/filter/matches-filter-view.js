define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/matches-filter-template.html' ) );

	var service = require( '/resources/js/services/service.js' );
	var chosen = require( 'chosen' );

	var DateTimePickerView = require( 'js/controls/date-time-picker/date-time-picker' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Matches: Settings'
		, buttonSaveSettingsLabel: 'Matches: Settings: save button'
		, buttonCancelSettingsLabel: 'Matches: Settings: cancel button'
		, userLabel: 'User'
		, categoryLabel: 'Category'
		, cupLabel: 'Configurable view / Filter: Cup'
		, teamLabel: 'Team'
		, matchDateLabel: 'Configurable view / Filter: Match date'
		, showFutureMatchesLabel: 'Configurable view / Filter: Show future matches'
		, showFinishedLabel: 'Configurable view / Filter: Show finished matches'
	} );

	return Backbone.View.extend( {

		events: {
			'change #settings-user-id': '_onUserChange'
			, 'change #settings-category-id': '_onCategoryChange'
			, 'change #settings-cup-id': '_onCupChange'
			, 'change #settings-team-id': '_onTeamChange'
			, 'change #settings2-team-id': '_onTeam2Change'
			, 'click #filter-by-match-date-enabled': '_onFilterByDateCheckboxClick'
			, 'change #settings-show-future-matches': '_onShowFutureChange'
			, 'change #settings-show-finished': '_onShowFinishedChange'
		},

		initialize: function ( options ) {
			this.categories = service.loadCategories();
			this.cups = options.cups;
			this.teams = service.loadTeams();
			this.users = service.loadUsers();

			if ( ! this.model.get( 'filterByDate' ) ) {
				this.model.set( { filterByDate: new Date() } )
			}
		},

		render: function() {
			var model = this.model.toJSON();

			var categoryId = this.model.get( 'categoryId' );

			this.$el.html( template( {
				model: model
				, users: this.users
				, categories: this.categories
				, cups: service.filterCupsByCategory( this.cups, categoryId )
				, teams: service.filterTeamsByCategory( this.teams, categoryId )
				, showFutureMatches: model.showFutureMatches
				, showFinished: model.showFinished
				, translator: translator
			} ) );

			var options = {
				width: "100%"
			};
			this.$( '#settings-user-id' ).chosen( options );
			this.$( '#settings-category-id' ).chosen( options );
			this.$( '#settings-cup-id' ).chosen( options );
			this.$( '#settings-team-id' ).chosen( options );
			this.$( '#settings2-team-id' ).chosen( options );

			this.dateTimePickerView = new DateTimePickerView( {
				el: this.$( '.js-filter-by-match-date' )
				, initialValue: this.model.get( 'filterByDate' )
				, disableTime: true
				, datTimeChangeCallback: this._onFilterByDateChange.bind( this )
			});

			return this;
		},

		_userChange: function( userId ) {
			this.model.set( { userId: userId } );
			this.render();
		},

		_categoryChange: function( categoryId ) {
			this.model.set( { categoryId: categoryId, cupId: 0, teamId: 0, team2Id: 0 } );
			this.render();
		},

		_cupChange: function( cupId ) {
			this.model.set( { cupId: cupId } );
			this.render();
		},

		_teamChange: function( teamId ) {
			this.model.set( { teamId: teamId } );
			this.render();
		},

		_team2Change: function( teamId ) {
			this.model.set( { team2Id: teamId } );
			this.render();
		},

		_showFutureChange: function( val ) {
			this.model.set( { showFutureMatches: val } );
		},

		_showFinishedChange: function( val ) {
			this.model.set( { showFinished: val } );
		},

		_onUserChange: function( evt ) {
			evt.preventDefault();

			this._userChange( $( evt.target ).val() );
		},

		_onCategoryChange: function( evt ) {
			evt.preventDefault();

			this._categoryChange( $( evt.target ).val() );
		},

		_onCupChange: function( evt ) {
			evt.preventDefault();

			this._cupChange( $( evt.target ).val() );
		},

		_onTeamChange: function( evt ) {
			evt.preventDefault();

			this._teamChange( $( evt.target ).val() );
		},

		_onTeam2Change: function( evt ) {
			evt.preventDefault();

			this._team2Change( $( evt.target ).val() );
		},

		_onFilterByDateCheckboxClick: function( evt ) {
			evt.preventDefault();

			this.model.set( {
				filterByDateEnable: this.$( '#filter-by-match-date-enabled' ).is( ':checked' )
			} );

			this.render();
		},

		_onFilterByDateChange: function( date ) {
			this.model.set( {
				filterByDate: date
			} );
		},

		_onShowFutureChange: function( evt ) {
			evt.preventDefault();

			this._showFutureChange( this.$( '#settings-show-future-matches' ).is(':checked') );
		},

		_onShowFinishedChange: function( evt ) {
			evt.preventDefault();

			this._showFinishedChange( this.$( '#settings-show-finished' ).is(':checked') );
		}
	});
} );