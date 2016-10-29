define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var UserDataModel = Backbone.Model.extend( {

		password_confirmation: '',

		defaults: {
			id: 0
			, login: ''
			, name: ''
			, password: ''
		},

		initialize:function ( options ) {
			this.url = '/rest/users/create/'
		},

		refresh: function() {
			this.fetch( { reset: true } );
		}
	});

	return { UserDataModel: UserDataModel };
} );
