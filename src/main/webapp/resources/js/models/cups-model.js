define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var CupModel = Backbone.Model.extend( {

		idAttribute: 'cupId',

		defaults: {
			cupId: 0
			, cupName: ''
			, category: {}
			, winnersCount: 3
			, readyForCupBets: false
			, readyForMatchBets: false
			, finished: false
			, logoUrl: ''
		},

		initialize: function ( options ) {
		}
	});

	return Backbone.Collection.extend( {

		model: CupModel,

		initialize: function ( options ) {
			this.url = '/rest/cups/public/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});
} );
