define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	return Backbone.Model.extend( {

		defaults: {
			userId: 0
			, categoryId: 0
			, cupId: 0
			, teamId: 0
			, filterByDate: null
			, filterByDateEnable: false
			, showFutureMatches: true
			, showFinished: false
		},

		initialize: function ( options ) {

			this.settings = options;

			this.saveAttributes();
		},

		saveAttributes: function() {
			this.appliedAttributes = this.toJSON();
		},

		restoreAttributes: function() {
			this.set( this.appliedAttributes );
		},

		reset: function() {
			this.set( this.settings );
		}
	});
});