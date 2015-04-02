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
		},

		refresh: function() {
			this.fetch( { cache: false, async: false } );
		}
	});

	var UsersModel = Backbone.Collection.extend( {

		model: UserModel,

		initialize: function ( options ) {
			this.url = '/rest/users/';
		},

		refresh: function() {
			this.fetch( { cache: false, reset: true } );
		}
	});

	return { UserModel: UserModel, UsersModel: UsersModel };
} );

