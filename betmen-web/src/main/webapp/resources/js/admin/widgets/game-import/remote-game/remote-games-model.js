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
			this.on( 'events:select_deselect', this._selectDeselect, this );
		},

		url: function () {
			return '/admin/rest/remote-games-import/remote-game/' + this.get( 'remoteGameId' ) + '/?cupId=' + this.cupId;
		},

		_selectDeselect: function( isSelected ) {
			this.skipImport = ! isSelected;
		}
	} );

	var RemoteGamesModel = Backbone.Collection.extend( {

		model: RemoteGameModel,

		initialize: function ( options ) {
		}
	} );

	return { RemoteGamesModel: RemoteGamesModel, RemoteGameModel: RemoteGameModel };
} );
