define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/match-description-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		noMatchDescription: "No match description is given"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.match = options.match;
			this.render();
		},

		render: function () {
			var match = this.match;
			var description = match.description ? match.description : translator.noMatchDescription;
			var data = _.extend( {}, { description: description } );
			this.$el.html( template( data ) );
		}
	} );
} );
