define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	return Backbone.Model.extend( {

		letter: '',
		active: false,

		initialize: function ( options ) {
			this.cup = options.options.cup;
		},

		url: function () {
			return '/rest/cups/' + this.cup.cupId + '/teams/';
		},

		load: function() {
			this.letter = '';
			this.active = false;

			this.fetch( { cache:false } );
		},

		loadStartedWith: function( letter ) {
			this.letter = letter;
			this.active = false;

			this.fetch( { data: { letter: letter }, cache:false } );
		},

		loadActive: function( letter ) {
			this.letter = '';
			this.active = true;

			this.fetch( { data: { active: true }, cache:false } );
		}
	} );
} );