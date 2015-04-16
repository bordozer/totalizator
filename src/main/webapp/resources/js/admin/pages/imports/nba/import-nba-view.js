define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/import-nba-template.html' ) );

	var nbaImportService = require( './nba-import-service' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
		, startImport: "Start import"
		, stopImport: "Stop import"
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-import-start': '_onImportStart'
			, 'click .js-import-stop': '_onImportStop'
		},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.$el.html( template( data ) );

			return this;
		},

		_onImportStart: function( evt ) {
			evt.preventDefault();

			nbaImportService.startImport();
		},

		_onImportStop: function( evt ) {
			evt.preventDefault();

			nbaImportService.stopImport();
		}
	} );
} );