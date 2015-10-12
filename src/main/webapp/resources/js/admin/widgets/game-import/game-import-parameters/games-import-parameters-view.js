define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );
	var chosen = require( 'chosen' );

	var template = _.template( require( 'text!./templates/game-import-parameters-template.html' ) );

	var remoteGamesImportService = require( 'js/admin/widgets/game-import/remote-games-import-service' );

	var DateRangePickerView = require( 'js/controls/date-range-picker/date-range-picker' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		validation_SelectCup: "Select cup first!"
		, cupLabel: "Cup"
		, showActiveCupsOnly: "Show active cups only"
	} );

	return Backbone.View.extend( {

		showActiveCupsOnlyClick: true,

		events: {
			'change #selectedCupId': '_onCupSelect'
			, 'change .js-show-active-cups-only': '_onShowActiveCupsOnlyClick'
			, "change input[name='sportKindId']": '_onSportKindFilterChange'
		},

		initialize: function ( options ) {
			this.render();
		},

		render: function () {

			var model = this.model.toJSON();
			var cupId = model.cupId;

			var selectedSportKindId = model.selectedSportKindId;
			this.cupsForGameImport = this.showActiveCupsOnlyClick ? remoteGamesImportService.loadAllCurrentCupsConfiguredForRemoteGameImport( selectedSportKindId ) : remoteGamesImportService.loadCupsConfiguredForRemoteGameImport( selectedSportKindId );

			var data = _.extend( {}
					, model
					, {
						cupId: cupId
						, cupsForGameImport: this.cupsForGameImport
						, showActiveCupsOnlyClick: this.showActiveCupsOnlyClick
						, sportKinds: this.model.sportKinds
						, selectedSportKindId: selectedSportKindId
						, translator: translator
					}
			);

			this.$el.html( template( data ) );

			this.$( '#selectedCupId' ).chosen( { width: '100%' } );

			if ( cupId ) {

				this.dateRangePicker = new DateRangePickerView( {
					parameters: this.model.get( 'timePeriod' )
					, el: this.$( '.js-date-range-picker' )
				} );
				this.dateRangePicker.on( 'events:date_range_change', this._onDateRangeChange, this );
			}
		},

		_getSelectedCupId: function () {
			return this.$( '#selectedCupId' ).val();
		},

		_onShowActiveCupsOnlyClick: function () {
			this.showActiveCupsOnlyClick = this.$( ".js-show-active-cups-only" ).is( ':checked' );
			this.render();
		},

		_onSportKindFilterChange: function ( evt ) {
			this.model.set( { selectedSportKindId: $( evt.target ).val(), cupId: 0 } );
			this.render();
		},

		_onCupSelect: function () {

			var selectedCupId = this._getSelectedCupId();

			if ( selectedCupId == 0 ) {
				return;
			}

			this.model.set( { cupId: selectedCupId } );

			var selectedCup = _.find( this.cupsForGameImport, function ( cupForGameImport ) {
				return cupForGameImport.cup.cupId == selectedCupId;
			} );
			this.model.set( { timePeriod: { timePeriodType: selectedCup.timePeriodType } } );

			this.render();

			this.trigger( 'events:games_import_parameters_change', this._getParameters() );
		},

		_onDateRangeChange: function( dateRangeParameters ) {
			var parameters = this._getParameters();

			this.model.set( parameters );
			this.trigger( 'events:games_import_parameters_change', parameters );
		},

		_getParameters: function() {

			var parameters = {};

			parameters.cupId = this.model.get( 'cupId' );
			parameters.timePeriod = this.dateRangePicker.getParameters();

			return parameters;
		}
	} );
} );

