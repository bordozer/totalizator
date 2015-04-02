define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var PortalPageModel = Backbone.Model.extend( {

		defaults: {
			id: 0
			, userId: 0
			, userName: ''
			, cupsShowTo: []
		},

		initialize: function ( options ) {
			this.options = options.options;
			this.url = '/rest/portal-page/';
		}
	});

	return { PortalPageModel: PortalPageModel };
} );
