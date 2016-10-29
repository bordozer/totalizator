define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var LoginFormModel = Backbone.Model.extend( {

		defaults: {
			login: ''
			, name: ''
		},

		initialize:function ( options ) {
		}
	});

	return { LoginFormModel: LoginFormModel };
} );
