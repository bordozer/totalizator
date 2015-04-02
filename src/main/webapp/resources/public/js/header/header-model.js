define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var HeaderModel = Backbone.Model.extend( {

		initialize: function ( options ) {
		}
	});

	return { HeaderModel: HeaderModel };
} );
