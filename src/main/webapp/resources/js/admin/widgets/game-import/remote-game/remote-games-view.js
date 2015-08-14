define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var RemoteGameView = require( './remote-game-view' );

	var adminService = require( '/resources/js/admin/services/admin-servise.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noMatchesForImportFound: "No matches for import found"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {

			// model is RemoteGamesModel.RemoteGamesModel

			this.importParameters = options.importParameters;
			this.emptyRemoteGames = options.emptyRemoteGames;

			this.cup = adminService.loadCup( this.importParameters.cupId );

			this.listenTo( this.model, 'add', this._onAddNewRemoteGame );

			this.render();
		},

		render: function() {

			this.$el.empty();

			if ( this.emptyRemoteGames.length == 0 ) {
				this.$el.html( translator.noMatchesForImportFound );
				return;
			}

			var self = this;
			_.each( this.emptyRemoteGames, function( remoteGame ) {
				self._renderRemoteGame( remoteGame );
			});
		},

		_renderRemoteGame: function( emptyRemoteGame ) {
			this.model.add( emptyRemoteGame );
		},

		_onAddNewRemoteGame: function( model ) {

			model.cupId = this.cup.cupId;

			var el = $( "<div></div>" );
			this.$el.append( el );

			var view = new RemoteGameView( { model: model, el: el, options: { cup: this.cup } } );
			view.render();
			model.fetch( { cache: false } ); //async: false
		}
	});
} );

