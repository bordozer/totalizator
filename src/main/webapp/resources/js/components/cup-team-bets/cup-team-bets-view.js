define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-team-bets-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		cupResultBetsLabel: "cup result bets"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.cup = options.options.cup;
			this.render();
		},

		render: function () {

			this.$el.html( template( {
				cup: this.cup
				, translator: translator
			 } ) );

			return this;
		}

	});
} );
