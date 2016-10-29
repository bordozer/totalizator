define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var LoginModel = Backbone.Model.extend( {

		initialize: function ( options ) {
		}
	});

	return { LoginModel: LoginModel };
} );
