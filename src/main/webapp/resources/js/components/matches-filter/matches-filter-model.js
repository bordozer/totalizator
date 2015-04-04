define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	return Backbone.Model.extend( {

		defaults: {
			userId: 0
			, categoryId: 0
			, cupId: 0
			, teamId: 0
			, showFutureMatches: true
			, showFinished: false
		},

		initialize: function ( options ) {
		},

		reset: function() {
			this.set( {
				userId: 0
				, categoryId: 0
				, cupId: 0
				, teamId: 0
				, showFutureMatches: true
				, showFinished: false
			} );
		}
	});
});