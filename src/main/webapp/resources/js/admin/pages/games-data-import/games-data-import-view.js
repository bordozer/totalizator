define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/games-data-import-template.html' ) );

	var nbaImport = require( 'js/admin/widgets/imports/nba/import-nba' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {

		},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			this._renderNbaImport();

			return this;
		},

		_renderNbaImport: function() {
			nbaImport( this.$( '.js-nba-import' ), {} );
		}
	} );
} );