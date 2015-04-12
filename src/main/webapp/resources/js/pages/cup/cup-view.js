define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Cup overview"
		, bupBetsLabel: "Cup bets"
	} );

	return Backbone.View.extend( {

		events: {

		},

		initialize: function ( options ) {
			this.cupId = options.options.cupId;

			this.render();
		},

		render: function() {
			var data = _.extend( {}, this.model.toJSON(), { cupId: this.cupId, translator: translator } );
			this.$el.html( template( data ) );
		}
	} );
} );