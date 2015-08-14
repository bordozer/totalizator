define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var RemoteGameModel = Backbone.Model.extend( {

		idAttribute: 'remoteGameId',

		isProcessed: false,
		skipImport: false,
		importStatus: {},
		cupId: 0,

		defaults: {
			loaded: false
		},

		initialize: function ( options ) {
		},

		url: function () {
			return '/admin/rest/remote-games-import/remote-game/' + this.get( 'remoteGameId' ) + '/?cupId=' + this.cupId;
		}
	} );

	var RemoteGamesModel = Backbone.Collection.extend( {

		model: RemoteGameModel,

		initialize: function ( options ) {
		}
	} );

	return { RemoteGamesModel: RemoteGamesModel, RemoteGameModel: RemoteGameModel };
} );
