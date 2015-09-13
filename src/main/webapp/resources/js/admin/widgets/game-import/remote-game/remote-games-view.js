define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var RemoteGameView = require( './remote-game-view' );

	var adminService = require( '/resources/js/admin/services/admin-service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noMatchesForImportFound: "No matches for import found"
		, error: 'Can not get data from server'
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {

			// model is RemoteGamesModel.RemoteGamesModel

			this.importParameters = options.importParameters;
			this.emptyRemoteGames = options.emptyRemoteGames;

			this.cup = adminService.loadCup( this.importParameters.cupId );

			this.render();
		},

		render: function() {

			this.$el.empty();

			if ( this.emptyRemoteGames.length == 0 ) {
				this.$el.html( translator.noMatchesForImportFound );
				return;
			}

			var self = this;
			_.each( this.emptyRemoteGames, function( emptyRemoteGame ) {
				//console.log( emptyRemoteGame );
				self._renderRemoteGame( emptyRemoteGame );
			});
		},

		_renderRemoteGame: function( emptyRemoteGame ) {
			var model = this.model.add( emptyRemoteGame );
			this._onAddNewRemoteGame( model );
		},

		_onAddNewRemoteGame: function( model ) {

			model.cupId = this.cup.cupId;

			var el = $( "<div></div>" );
			this.$el.append( el );

			var view = new RemoteGameView( { model: model, el: el, options: { cup: this.cup } } );
			view.render();
			model.fetch( { cache: false, error: function( arg ) {
				alert( translator.error );
			} } );
		}
	});
} );

