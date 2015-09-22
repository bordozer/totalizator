define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/remote-game-template.html' ) );
	var templateLoading = _.template( require( 'text!./templates/remote-game-loading-template.html' ) );
	var templateError = _.template( require( 'text!./templates/remote-game-error-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		teamNotFoundInCategory: "Teams not found for selected category - will be created automatically if remote game are checked"
		, noLocalMatchFound: "Local match for the remote game not found"
		, theMatchIsAlreadyImported: "The match is already imported"
		, gettingGameData: "Getting game data"
		, error: 'Can not get data from server'
	} );

	return Backbone.View.extend( {

		events: {
			'change #gamesToImport': '_onRemoteGameSelect'
		},

		initialize: function ( options ) {
			this.cup = options.options.cup;

			this.model.on( 'sync', this.render, this );
			this.listenTo( this.model, 'events:remote_game_imported', this.render );
		},

		render: function() {

			var model = this.model.toJSON();

			if ( ! model.loaded ) {

				var _data = {
					remoteGameId: model.remoteGameId
					, translator: translator
				};

				this.$el.html( templateLoading( _data ) );

				var self = this;
				this.model.fetch( { cache: false, error: function( arg ) {
					self.$el.html( templateError( _data ) );
				} } );

				return;
			}

			var remoteGameLocalData = model.remoteGameLocalData;

			var match = remoteGameLocalData.match;
			var team1 = remoteGameLocalData.team1;
			var team2 = remoteGameLocalData.team2;

			var atLeastOneTeamNotFound = ! team1 || ! team2;

			var matchResults = { style1: '', style2: '' };
			if ( team1 && team2 ) {
				matchResults = service.matchResults( team1.teamId, model.score1, team2.teamId, model.score2 );
			}

			if ( atLeastOneTeamNotFound ) {
				this.model.skipImport = true;
			}

			if ( match && match.matchFinished && match.score1 == model.score1 && match.score2 == model.score2 ) {
				this.model.skipImport = true;
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
				, skipImport: this.model.skipImport
				, translator: translator
			} );

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

		_onRemoteGameSelect: function() {
			this.model.skipImport = ! this.$( "#gamesToImport" ).is(':checked');
		}
	});
} );
