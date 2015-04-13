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
		, cupFinished: "Cup finished"
	} );

	return Backbone.View.extend( {

		initialize: function ( options ) {
			this.cupId = options.options.cupId;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } );

			var model = this.model.toJSON();

			var data = _.extend( {}
					, model
					, {
						cupId: this.cupId
						, isCupFinished: model.finished
						, translator: translator
					}
			);

			this.$el.html( template( data ) );
		}
	} );
} );