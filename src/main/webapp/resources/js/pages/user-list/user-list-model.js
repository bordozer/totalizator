define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var UserModel = Backbone.Model.extend( {

		idAttribute: 'userId',

		userId: 0,

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
			return '/rest/users/';
		}
	});
} );
