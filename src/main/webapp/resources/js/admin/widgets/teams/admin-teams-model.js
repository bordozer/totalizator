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

	var TeamsModel = Backbone.Collection.extend( {

		model: TeamModel,
		selectedCupId: 0,

		initialize: function ( options ) {
			this.url = '/admin/rest/teams/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { TeamsModel: TeamsModel };
} );
