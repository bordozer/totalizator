define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/remote-games-template.html' ) );

	var RemoteGameView = require( './remote-game-view' );

	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noMatchesForImportFound: "No matches for import found"
		, allGamesLabel: "All games"
		, alreadyImportedGamesOnlyLabel: "Already imported games only"
		, newGamesOnlyLabel: "New games only"
		, noTeamGamesLabel: "No team(s) games"
		, finishedGamesLabel: "Finished games"
		, notFinishedGamesLabel: "Not finished games"
		, selectedForImportGamesLabel: "Selected for import games"
		, skippedForImportGamesLabel: "Skipped for import games"
		, gamesFoundLabel: "Games found"
		, gamesShownLabel: "Games shown"
		, selectAllLabel: "Select all"
		, deselectAllLabel: "Deselect all"
	} );

	return Backbone.View.extend( {

		remoteGameFilter: 1,

		events: {
			'click .js-select-all': '_selectAllRemoteGames'
			, 'click .js-deselect-all': '_deselectAllRemoteGames'
			, "click [name='remote-games-filter']": '_filterRemoteGames'
		},

		initialize: function ( options ) {

			this.importParameters = options.importParameters;
			this.emptyRemoteGames = options.emptyRemoteGames;

			this.cup = adminService.loadCup( this.importParameters.cupId );

			this._fillModel();
			this.render();
		},

		render: function () {

			if ( this.emptyRemoteGames.length == 0 ) {
				this.$el.html( translator.noMatchesForImportFound );
				return;
			}

			this.$el.html( template( { remoteGameFilter: this.remoteGameFilter, translator: translator } ) );

			var self = this;
			var gamesShown = 0;
			this.model.forEach( function ( model ) {

				if ( ! self._isModelInFilter( model ) ) {
					return;
				}

				self._renderRemoteGame( model );

				gamesShown++;
			} );

			this.$( '.js-games-found' ).text( this.model.length );
			this.$( '.js-games-shown' ).text( gamesShown );
		},

		_isModelInFilter: function( model ) {

			var json = model.toJSON();

			var remoteGameLocalData = json.remoteGameLocalData;

			var showOnlyAlreadyImportedGames = this.remoteGameFilter == 2;
			var showOnlyNewGames = this.remoteGameFilter == 3;
			var showOnlyGamesWithoutTeamsFound = this.remoteGameFilter == 4;
			var showOnlFinishedGames = this.remoteGameFilter == 5;
			var showOnlyNotFinishedGames = this.remoteGameFilter == 6;
			var checkedForImportGames = this.remoteGameFilter == 7;
			var notCheckedForImportGames = this.remoteGameFilter == 8;

			var match = remoteGameLocalData.match;
			var team1 = remoteGameLocalData.team1;
			var team2 = remoteGameLocalData.team2;
			var isGameImported = match != null;

			if ( showOnlyAlreadyImportedGames && ! isGameImported ) {
				return false;
			}

			if ( showOnlyNewGames && isGameImported ) {
				return false;
			}

			if ( showOnlyGamesWithoutTeamsFound && team1 && team2 ) {
				return false;
			}

			if ( showOnlFinishedGames && ! json.finished ) {
				return false;
			}

			if ( showOnlyNotFinishedGames && json.finished ) {
				return false;
			}

			if ( checkedForImportGames && model.skipImport ) {
				return false;
			}

			if ( notCheckedForImportGames && ! model.skipImport ) {
				return false;
			}

			return true
		},

		_fillModel: function() {
			var self = this;
			_.each( this.emptyRemoteGames, function ( emptyRemoteGame ) {
				self.model.add( emptyRemoteGame );
			});
		},

		_renderRemoteGame: function ( model ) {

			model.cupId = this.cup.cupId;

			var el = $( "<div></div>" );
			this.$el.append( el );

			var view = new RemoteGameView( { model: model, el: el, options: { cup: this.cup } } );
			view.render();
		},

		_filterRemoteGames: function ( evt ) {
			this.remoteGameFilter = $( evt.target ).data( 'filter-by' )
			this.render();
		},

		_selectAllRemoteGames: function () {
			this._checkRemoteGame( true );
		},

		_deselectAllRemoteGames: function () {
			this._checkRemoteGame( false );
		},

		_checkRemoteGame: function ( isSelected ) {

			var self = this;
			this.model.forEach( function ( model ) {

				if ( ! self._isModelInFilter( model ) ) {
					return;
				}

				model.trigger( 'events:select_deselect', isSelected );
			} );

			_.each( this.$( '.js-games-to-import' ), function ( checkbox ) {
				$( checkbox ).prop( "checked", isSelected );
			} );
		}
	} );
} );

