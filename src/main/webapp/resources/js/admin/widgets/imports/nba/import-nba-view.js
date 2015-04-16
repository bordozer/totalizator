define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/import-nba-template.html' ) );

	var service = require( '/resources/js/services/service.js' );
	var nbaImportService = require( './nba-import-service' );

	var chosen = require( 'chosen' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "NBA games data import"
		, startImport: "Start import"
		, stopImport: "Stop import"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-import-start': '_onImportStart'
			, 'click .js-import-stop': '_onImportStop'
		},

		initialize: function ( options ) {
			this.render();
		},

		renderBody: function () {

			this.importStatus = nbaImportService.isImportingNow();
			this.footerText( this.importStatus.importMessage );

			var data = _.extend( {}, this.model.toJSON(), {
				isImportingNow: this.importStatus.requestSuccessful
				, cups: service.loadCups()
				,translator: translator
			} );

			this.$( this.windowBodyContainerSelector ).html( template( data ) );

			this.$( '#selectedCupId' ).chosen( { width: 100 } );

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-exchange';
		},

		_startImport: function() {
			var result = nbaImportService.startImport( this.$( '#selectedCupId' ).val() );

			this.renderBody();

			this.footerText( result.importMessage );
		},

		_stopImport: function() {
			var result = nbaImportService.stopImport();

			this.renderBody();

			this.footerText( result.importMessage );
		},

		_onImportStart: function( evt ) {
			evt.preventDefault();

			this._startImport();
		},

		_onImportStop: function( evt ) {
			evt.preventDefault();

			this._stopImport();
		}
	} );
} );