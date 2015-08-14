define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/games-data-import-template.html' ) );

	var RemoteGamesImportView = require( 'js/admin/widgets/game-import/remote-games-import-view' );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON() );

			this.$el.html( template( data ) );

			this._renderRemoteGamesImport();

			return this;
		},

		_renderRemoteGamesImport: function() {
			var view = new RemoteGamesImportView( { el: this.$( '.js-remote-games-import' ) } );
		}
	} );
} );