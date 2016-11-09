define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/admin-template.html' ) );

	var RemoteGamesImportView = require( 'js/admin/widgets/game-import/remote-games-import-view' );
	var AdminMatchesWidget = require( 'js/admin/widgets/matches/admin-matches-widget' );

	var AdminView = Backbone.View.extend( {

		initialize: function( options ) {
			this.options = options.options;
			this.render();
		},

		render: function() {
			this.$el.html(template());
			this._renderGamesImport();
			this._renderGames();
		},

		_renderGamesImport: function() {
			new RemoteGamesImportView( { el: this.$( '.js-admin-page-games-import' ) } );
		},

		_renderGames: function() {
			new AdminMatchesWidget( this.$( '.js-admin-page-games' ), this.options );
		}
	} );

	return { AdminView: AdminView };
} );

