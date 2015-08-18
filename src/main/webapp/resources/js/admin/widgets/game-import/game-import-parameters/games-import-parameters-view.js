define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/game-import-parameters-template.html' ) );

	var adminService = require( '/resources/js/admin/services/admin-service.js' );
	var dateTimeService = require( '/resources/js/services/date-time-service.js' );

	var DateTimePickerView = require( 'js/controls/date-time-picker/date-time-picker' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		importStep_CollectGamesData: "Collect games data"
		, validation_SelectCup: "Select cup first!"
		, cupLabel: "Cup"
		, dateFromLabel: "Date from"
		, dateToLabel: "Date to"
		, showActiveCupsOnly: "Show active cups only"
	} );

	return Backbone.View.extend( {

		showActiveCupsOnlyClick: true,

		events: {
			'change #selectedCupId': '_onCupSelect'
			, 'change .js-show-active-cups-only': '_onShowActiveCupsOnlyClick'
		},

		initialize: function ( options ) {

			var today = dateTimeService.dateNow();

			this.model.set( {
				cupId: 0
				, dateFrom: today
				, dateTo: today
			} );

			this.render();
		},

		render: function() {

			var cups = this.showActiveCupsOnlyClick ? adminService.loadAllCurrentCups() : adminService.loadCups();

			var model = this.model.toJSON();

			var data = _.extend( {}, model, {
				cupId: model.cupId
				, cups: cups
				, showActiveCupsOnlyClick: this.showActiveCupsOnlyClick
				, translator: translator
			} );

			this.$el.html( template( data ) );

			this.$( '#selectedCupId' ).chosen( { width: '100%' } );

			var onDateFromSelectCallback = this._onDateFromSelect.bind( this );
			this.dateTimePickerFromView = new DateTimePickerView( {
				el: this.$( '.js-date-from' )
				, initialValue: this.model.get( 'dateFrom' )
				, disableTime: true
				, datTimeChangeCallback: onDateFromSelectCallback
			} );

			var onDateToSelectCallback = this._onDateToSelect.bind( this );
			this.dateTimePickerToView = new DateTimePickerView( {
				el: this.$( '.js-to-date' )
				, initialValue: this.model.get( 'dateTo' )
				, disableTime: true
				, datTimeChangeCallback: onDateToSelectCallback
			} );
		},

		_getSelectedCupId: function() {
			return this.$( '#selectedCupId' ).val();
		},

		_onCupSelect: function() {
			this.model.set( { cupId: this._getSelectedCupId() } );
		},

		_onDateFromSelect: function( date ) {
			this.model.set( { dateFrom: date } );
		},

		_onDateToSelect: function( date ) {
			this.model.set( { dateTo: date } );
		},

		_onShowActiveCupsOnlyClick: function() {
			this.showActiveCupsOnlyClick = this.$( ".js-show-active-cups-only" ).is(':checked');
			this.render();
		}
	});
} );

