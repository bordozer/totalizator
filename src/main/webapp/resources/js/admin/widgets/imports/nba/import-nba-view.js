define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/import-nba-template.html' ) );

	var nbaImportService = require( './nba-import-service' );

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

			var data = _.extend( {}, this.model.toJSON(), {
				isImportingNow: this.importStatus.requestSuccessful
				, statusMessage: this.importStatus.importMessage
				,translator: translator
			} );

			this.$( this.windowBodyContainerSelector ).html( template( data ) );

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
			var result = nbaImportService.startImport();
			this.renderBody();
			/*if ( result.requestSuccessful ) {
				this.$( '.js-import-start-icon' ).removeClass( 'fa-exchange' );
				this.$( '.js-import-start-icon' ).addClass( 'fa-refresh fa-spin' );
			}*/
		},

		_stopImport: function() {
			var result = nbaImportService.stopImport();
			this.renderBody();
			/*if ( result.requestSuccessful ) {
				this.$( '.js-import-start-icon' ).removeClass( 'fa-refresh fa-spin' );
				this.$( '.js-import-start-icon' ).addClass( 'fa-exchange' );
			}*/
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