define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var CupWinnerModel = Backbone.Model.extend( {

		defaults: {

		},

		initialize: function ( options ) {
		}
	});

	return Backbone.Collection.extend( {

		model: CupWinnerModel,

		initialize: function ( options ) {
			this.cup = options.options.cup;
		},

		url: function() {
			return '/rest/cups/' + this.cup.cupId + '/winners/';
		},

		refresh: function( data ) {
			this.fetch( { data: data, cache: false, reset: true} );
		}
	});
} );
