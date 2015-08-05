define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-description-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Matches"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.match = options.match;
			this.render();
		},

		render: function () {
			var data = _.extend( {}, { match: this.match } );
			this.$el.html( template( data ) );
		}
	} );
} );
