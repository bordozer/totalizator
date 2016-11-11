define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var UserModel = Backbone.Model.extend( {

		idAttribute: 'userId',

		defaults: {
			userId: 0
			, userName: ''
		},

		initialize: function ( options ) {
		}
	});

	return Backbone.Collection.extend( {

		model: UserModel,

		initialize: function ( options ) {
		},

		url: function() {
			return '/rest/user-groups/all-members-of-all-my-groups/';
		}
	});
} );
