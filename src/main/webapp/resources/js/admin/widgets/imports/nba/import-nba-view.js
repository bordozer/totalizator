define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/import-nba-template.html' ) );

	var service = require( '/resources/js/services/service.js' );
	var nbaImportService = require( './nba-import-service' );
	var autorefreshService = require( 'js/services/auto-refresh-service' );

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

			var importStatus = nbaImportService.getImportStatus();
			this.footerText( importStatus.importStatusMessage );

			var isImportingNow = importStatus.importActive;

			var data = _.extend( {}, this.model.toJSON(), {
				isImportingNow: isImportingNow
				, cupId: importStatus.cupId
				, importButtonTitle: isImportingNow ? importStatus.importStatusMessage : translator.startImport
				, cups: service.loadCups()
				,translator: translator
			} );

			this.$( this.windowBodyContainerSelector ).html( template( data ) );

			this.$( '#selectedCupId' ).chosen( { width: 100 } );

			if ( isImportingNow ) {
				this._scheduleAutorefresh();
			}

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

			this._scheduleAutorefresh();

			this.renderBody();

			this.footerText( result.importStatusMessage );
		},

		_stopImport: function() {
			var result = nbaImportService.stopImport();
			autorefreshService.stop();

			this.renderBody();

			this.footerText( result.importStatusMessage );
		},

		_scheduleAutorefresh: function() {

			var self = this;
			var autorefresh = function() {
				self.renderBody();
			};

			var exec = _.bind( autorefresh, this );
			autorefreshService.start( 10, exec );
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