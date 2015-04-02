define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var MainMenuModel = Backbone.Model.extend( {

		initialize: function ( options ) {
		}
	});

	return { MainMenuModel: MainMenuModel };
} );
