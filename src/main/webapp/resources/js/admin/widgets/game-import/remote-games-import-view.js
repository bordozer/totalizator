define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var chosen = require( 'chosen' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var GamesImportParametersModel = require( './game-import-parameters/games-import-parameters-model' );
	var GamesImportParametersView = require( './game-import-parameters/games-import-parameters-view' );

	var RemoteGamesModel = require( './remote-game/remote-games-model' );
	var RemoteGamesView = require( './remote-game/remote-games-view' );

	var adminService = require( '/resources/js/admin/services/admin-service.js' );
	var remoteGamesImportService = require( 'js/admin/widgets/game-import/remote-games-import-service' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Remote games import"
		, importStep_CollectGamesData: "Load games data"
		, startImport: "Start import"
		, stopImport: "Stop import"
		, cancelImport: "Cancel import"
		, breakImport: "Break import"
		, runAnotherImport: "Run another import"
		, validation_SelectCup: "Select cup first!"
		, importStatusBrokenByUser: "Broken by user"
		, importStatusMarkedAsSkipImport: "Marked as skip import"
		, importStatusTeamsDoNotExist: "The teams do not exist. Import impossible."
		, importStatusImportedSuccessfully: "Imported successfully"
	} );

	var MODE_PARAMETERS_FORM = 1;
	var MODE_READY_FOR_IMPORT = 2;
	var MODE_IMPORT_IN_PROGRESS = 3;
	var MODE_IMPORT_DONE = 4;

	return WidgetView.extend( {

		mode: 1,
		emptyRemoteGames: [],

		isBrokenByUser: false,

		events: {
			'click .js-load-remote-games-data': '_onLoadRemoteGamesDataClick'
			, 'click .js-remote-games-import-start': '_onRemoteGamesImportStartClick'
			, 'click .js-remote-games-import-break': '_onRemoteGamesImportBreakClick'
			, 'click .js-remote-games-import-close': '_onRemoteGamesImportCloseClick'
		},

		initialize: function ( options ) {

			this.importParameters = new GamesImportParametersModel();

			this.remoteGames = new RemoteGamesModel.RemoteGamesModel();

			this.render();
		},

		renderBody: function () {

			var view = this._getView();

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function () {

			if ( this.mode == MODE_PARAMETERS_FORM ) {
				return translator.title;
			}

			var parameters = this.importParameters.toJSON();
			var cup = adminService.loadCup( parameters.cupId );

			return translator.title + ': ' + cup.cupName + ', ' + parameters.dateFrom + ' - ' + parameters.dateTo;
		},

		getIcon: function () {
			return 'fa-exchange';
		},

		getCustomMenuItems: function() {

			if ( this.mode == MODE_READY_FOR_IMPORT ) {
				return [
					{ selector: 'js-remote-games-import-start', icon: 'fa fa-play', link: '#', text: translator.startImport, button: true }
					, { selector: 'js-remote-games-import-close', icon: 'fa fa-close', link: '#', text: translator.cancelImport, button: true }
				];
			}

			if ( this.mode == MODE_IMPORT_DONE ) {
				return [ { selector: 'js-remote-games-import-close', icon: 'fa fa-reply', link: '#', text: translator.runAnotherImport, button: true } ];
			}

			if ( this.mode == MODE_IMPORT_IN_PROGRESS ) {
				return [ { selector: 'js-remote-games-import-break', icon: 'fa fa-stop', link: '#', text: translator.breakImport, button: true } ];
			}

			return [ { selector: 'js-load-remote-games-data', icon: 'fa fa-download', link: '#', text: translator.importStep_CollectGamesData, button: true } ];
		},

		_getView: function() {

			var el = this.$( this.windowBodyContainerSelector );

			if ( this.mode === MODE_READY_FOR_IMPORT
					|| this.mode == MODE_IMPORT_IN_PROGRESS
					|| this.mode == MODE_IMPORT_DONE
			) {

				return new RemoteGamesView( {
					model: this.remoteGames
					, el: el
					, importParameters: this.importParameters.toJSON()
					, emptyRemoteGames: this.emptyRemoteGames
				} );
			}

			return new GamesImportParametersView( { model: this.importParameters, el: el } );
		},

		_onLoadRemoteGamesDataClick: function() {

			var importParameters = this.importParameters.toJSON();

			if ( importParameters.cupId == 0 ) {
				alert( translator.validation_SelectCup );
				return;
			}

			this.mode = MODE_READY_FOR_IMPORT;

			this.remoteGames.reset();
			this.emptyRemoteGames = remoteGamesImportService.loadRemoteGameIds( importParameters );

			this.render();
		},

		_onRemoteGamesImportStartClick: function() {

			if ( ! confirm( translator.startImport + '?' ) ) {
				return;
			}

			this.mode = MODE_IMPORT_IN_PROGRESS;
			this.isBrokenByUser = false;

			var self = this;

			this.remoteGames.forEach( function( model ) {

				model.isProcessed = true;

				if ( self.isBrokenByUser ) {
					model.importStatus = { icon: 'fa fa-stop', title: translator.importStatusBrokenByUser, isRemoteGameImported: false };
					model.trigger( 'events:remote_game_imported' );

					return;
				}

				if ( model.skipImport ) {
					model.importStatus = { icon: 'fa fa-ban', title: translator.importStatusMarkedAsSkipImport, isRemoteGameImported: false };
					model.trigger( 'events:remote_game_imported' );

					return;
				}

				/*if ( ! model.remoteGameLocalData.team1 || ! model.remoteGameLocalData.team2 ) {
					model.importStatus = { icon: 'fa fa-circle-thin', title: translator.importStatusTeamsDoNotExist, isRemoteGameImported: false };
					model.trigger( 'events:remote_game_imported' );
					return;
				}*/

				model.save();

				model.importStatus = { icon: 'fa fa-check-square-o', title: translator.importStatusImportedSuccessfully, isRemoteGameImported: true };

				model.trigger( 'events:remote_game_imported' );
			});

			this.mode = MODE_IMPORT_DONE;
			this._renderDropDownMenu();
		},

		_onRemoteGamesImportBreakClick: function() {
			this.isBrokenByUser = true;
		},

		_onRemoteGamesImportCloseClick: function() {
			this.mode = MODE_PARAMETERS_FORM;
			this.render();
		}
	} );
} );