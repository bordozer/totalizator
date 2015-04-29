define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/import-nba-template.html' ) );

	var adminService = require( '/resources/js/admin/services/admin-servise.js' );
	var nbaImportService = require( './nba-import-service' );
	var autorefreshService = require( 'js/services/auto-refresh-service' );

	var chosen = require( 'chosen' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "NBA games data import"
		, startImport: "Start import"
		, stopImport: "Stop import"
		, validation_SelectCup: "Select cup first!"
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

			var cups = adminService.loadCups();

			var data = _.extend( {}, this.model.toJSON(), {
				isImportingNow: isImportingNow
				, cupId: importStatus.cupId
				, importButtonTitle: isImportingNow ? importStatus.importStatusMessage : translator.startImport
				, cups: cups
				,translator: translator
			} );

			this.$( this.windowBodyContainerSelector ).html( template( data ) );

			this.$( '#selectedCupId' ).chosen( { width: '100%' } );

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

		_getSelectedCupId: function() {
			return this.$( '#selectedCupId' ).val();
		},

		_startImport: function() {

			var cupId = this._getSelectedCupId();
			if ( cupId == 0 ) {
				alert( translator.validation_SelectCup );
				return;
			}

			var result = nbaImportService.startImport( cupId );

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

			if ( ! confirm( translator.stopImport + '?' ) ) {
				return;
			}

			this._stopImport();
		}
	} );
} );