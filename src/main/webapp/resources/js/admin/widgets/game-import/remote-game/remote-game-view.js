define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/remote-game-template.html' ) );
	var templateLoading = _.template( require( 'text!./templates/remote-game-loading-template.html' ) );

	var service = require( '/resources/js/services/service.js' );
	var remoteGamesImportService = require( 'js/admin/widgets/game-import/remote-games-import-service' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		teamNotFoundInCategory: "Teams not found for selected category - will be created automatically if remote game are checked"
		, noLocalMatchFound: "Local match for the remote game not found"
		, theMatchIsAlreadyImported: "The match is already imported"
		, gettingGameData: "Getting game data"
	} );

	return Backbone.View.extend( {

		events: {
			'change #gamesToImport': '_onRemoteGameSelect'
		},

		initialize: function ( options ) {
			this.cup = options.options.cup;

			this.model.on( 'sync', this._renderRemoteGame, this );
			this.listenTo( this.model, 'events:remote_game_imported', this.render );
		},

		render: function() {

			var model = this.model.toJSON();

			if ( ! model.loaded ) {

				this.$el.html( templateLoading( {
					remoteGameId: model.remoteGameId
					, translator: translator
				} ) );

				return;
			}

			var remoteGameLocalData = this.model.remoteGameLocalData;

			var match = remoteGameLocalData.match;
			var team1 = remoteGameLocalData.team1;
			var team2 = remoteGameLocalData.team2;

			var atLeastOneTeamNotFound = ! team1 || ! team2;

			var matchResults = { style1: '', style2: '' };
			if ( team1 && team2 ) {
				matchResults = service.matchResults( team1.teamId, model.score1, team2.teamId, model.score2 );
			}

			var data = _.extend( {}, model, {
				match: match
				, team1: team1
				, team2: team2
				, matchBeginningTime: model.beginningTime
				, isReadyForImport: ! this.model.isProcessed
				, import_status: this.model.importStatus
				, importImpossible: atLeastOneTeamNotFound
				, matchResults: matchResults
				, panelColor: this._getPanelColor( match, atLeastOneTeamNotFound )
				, translator: translator
			} );

			if ( atLeastOneTeamNotFound ) {
				this.model.skipImport = true;
			}

			this.$el.html( template( data ) );
		},

		_getPanelColor: function( match, atLeastOneTeamNotFound ) {

			if ( this.model.importStatus.isRemoteGameImported ) {
				return 'panel-success';
			}

			if ( match ) {
				return 'panel-info';
			}

			if ( atLeastOneTeamNotFound ) {
				return 'panel-danger';
			}

			return 'panel-default'
		},

		_renderRemoteGame: function() {
			this.model.remoteGameLocalData = remoteGamesImportService.loadLocalDataForRemoteGame( this.model.toJSON(), this.cup.cupId );
			this.render();
		},

		_onRemoteGameSelect: function() {
			this.model.skipImport = ! this.$( "#gamesToImport" ).is(':checked');
		}
	});
} );
