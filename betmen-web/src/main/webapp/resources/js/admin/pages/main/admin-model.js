define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var AdminModel = Backbone.Model.extend( {

		initialize: function ( options ) {
			this.url = '/admin/rest/';
		}
	});

	return { AdminModel: AdminModel };
} );
