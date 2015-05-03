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
			console.log( this.settings );

			this.saveAttributes();
		},

		saveAttributes: function() {
			this.appliedAttributes = this.toJSON();
		},

		restoreAttributes: function() {
			this.set( this.appliedAttributes );
		},

		reset: function() {

			var settings = this.settings;

			if ( ! settings.userId ) {
				settings.userId = 0;
			}

			if ( ! settings.categoryId ) {
				settings.categoryId = 0;
			}

			if ( ! settings.cupId ) {
				settings.cupId = 0;
			}

			if ( ! settings.teamId ) {
				settings.teamId = 0;
			}

			if ( ! settings.team2Id ) {
				settings.team2Id = 0;
			}

			if ( ! settings.filterByDateEnable ) {
				settings.filterByDateEnable = false;
			}

			if ( ! settings.showFutureMatches ) {
				settings.showFutureMatches = false;
			}

			if ( ! settings.showFinished ) {
				settings.showFinished = false;
			}

			this.set( settings );
		}
	});
});