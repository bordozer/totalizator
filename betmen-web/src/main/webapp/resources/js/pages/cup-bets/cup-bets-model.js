define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );

	var CupPageModel = Backbone.Model.extend( {

		cupId: 0,

		defaults: {
			id: 0
			, userId: 0
			, userName: ''
			, cupsShowTo: []
		},

		initialize: function ( options ) {
			this.cupId = options.options.cupId;
		},

		url: function() {
			return '/rest/cups/' + this.cupId + '/';
		}
	});

	return { CupPageModel: CupPageModel };
} );
