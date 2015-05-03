define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	return Backbone.Model.extend( {

		defaults: {
			userId: 0
			, categoryId: 0
			, cupId: 0
			, teamId: 0
			, team2Id: 0
			, filterByDate: new Date()
			, filterByDateEnable: false
			, showFutureMatches: false
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
			this.set( _.extend( this.defaults, this.settings ) );
		}
	});
});