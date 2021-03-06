define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var CupModel = Backbone.Model.extend( {

		idAttribute: 'cupId',

		defaults: {
			cupId: 0
			, cupName: ''
			, categoryId: 0
			, winnersCount: 3
			, cupStartDate: new Date()
			, readyForCupBets: false
			, readyForMatchBets: false
			, finished: false
			, publicCup: false
			, logoUrl: ''
			, isEditState: false
			, cupWinners: []
			, cupPointsCalculationStrategyId: 1
		},

		initialize: function ( options ) {
			this.on( 'events:save-attributes', this._saveAttributes, this );
			this.on( 'events:restore-attributes', this._restoreAttributes, this );
		},

		setEditState: function() {
			this.set( { isEditState: true } );
		},

		cancelEditState: function() {
			this.set( { isEditState: false } );
		},

		_saveAttributes: function() {
			this._attrs = this.toJSON();
		},

		_restoreAttributes: function() {
			this.set( this._attrs );
		}
	});

	var CupsModel = Backbone.Collection.extend( {

		model: CupModel,

		selectedCup: { cupId: 0 },

		initialize: function ( options ) {
			this.url = '/admin/rest/cups/edit/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { CupsModel: CupsModel, CupModel: CupModel };
} );
