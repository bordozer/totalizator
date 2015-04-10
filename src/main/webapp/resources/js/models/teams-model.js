define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var TeamModel = Backbone.Model.extend( {

		idAttribute: 'teamId',

		defaults: {
			teamId: 0
			, teamName: ''
			, category: {}
		},

		initialize: function ( options ) {
		}
	});

	return Backbone.Collection.extend( {

		model: TeamModel,

		initialize: function ( options ) {
			this.url = '/rest/teams/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});
} );
