define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var TeamModel = Backbone.Model.extend( {

		idAttribute: 'teamId',

		filterByCategory: 0,

		defaults: {
			teamId: 0
			, teamName: ''
			, categoryId: 0
			, isEditState: false
			, teamChecked: true
			, matchCount: 0
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

	return Backbone.Collection.extend( {

		model: TeamModel,
		selectedCup: { cupId: 0 },

		initialize: function ( options ) {
		},

		url: function() {
			return '/admin/rest/teams/cups/' + this.selectedCup.cupId + '/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});
} );
