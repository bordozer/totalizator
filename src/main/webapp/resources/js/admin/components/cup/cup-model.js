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
			, showOnPortalPage: false
			, logoUrl: ''
			, isEditState: false
		},

		initialize: function ( options ) {
		},

		setEditState: function() {
			this.set( { isEditState: true } );
		},

		cancelEditState: function() {
			this.set( { isEditState: false } );
		}
	});

	var CupsModel = Backbone.Collection.extend( {

		model: CupModel,

		initialize: function ( options ) {
			this.url = '/admin/rest/cups/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { CupsModel: CupsModel, CupModel: CupModel };
} );
