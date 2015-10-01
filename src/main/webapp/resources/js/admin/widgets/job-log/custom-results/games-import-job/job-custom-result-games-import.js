define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var adminJobTaskLogService = require( 'js/admin/services/admin-job-task-log-service' );

	var template = _.template( require( 'text!./templates/job-custom-result-games-import-template.html' ) );
	var templateImportedGamesList = _.template( require( 'text!./templates/imported-games-list-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		totalStepsLabel: "Job task: Total steps"
		, performedStepsLabel: "Job task: Performed steps"
		, importedGamesLabel: "Job task: Imported games"
		, matchNotFoundLabel: "Job task: Imported match not found, it must have been deleted"
		, showImportedGamesLabel: "Job task: Show imported games"
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-show-imported-game-list': '_onShowImportedGameListClick'
		},

		initialize: function ( options ) {
			this.jobTaskLogId = options.jobTaskLogId;
			this.parameters = options.parameters;
		},

		render: function () {

			var data = _.extend( {}, {
				totalSteps: this.parameters.totalSteps
				, performedSteps: this.parameters.performedSteps
				, importedGamesCount: this.parameters.importedRemoteGames.length
				, translator: translator
			} );

			this.$el.html( template( data ) );

			return this;
		},

		_onShowImportedGameListClick: function () {

			this.$( '.js-imported-game-list' ).html( "<i class='fa fa-spinner fa-spin fa-2x'></i>" );

			var importedRemoteGames = this.parameters.importedRemoteGames;

			var importedGameIds = _.map( importedRemoteGames, function ( importedRemoteGames ) {
				return importedRemoteGames.importGameId;
			} );

			adminJobTaskLogService.loadImportedByJobRemoteGameMatchPairsAsync( this.jobTaskLogId, this._onImportedGameListLoaded.bind( this ) );
		},

		_onImportedGameListLoaded: function ( importedByJobRemoteGameMatchPairs ) {

			var data = _.extend( {}, {
				importedByJobRemoteGameMatchPairs: importedByJobRemoteGameMatchPairs
				, translator: translator
			} );

			this.$( '.js-imported-game-list' ).html( templateImportedGamesList( data ) )
		}
	} );
} );