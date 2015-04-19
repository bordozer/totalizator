define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-winners-bets-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: ""
	} );

	return Backbone.View.extend( {

		events: {

		},

		initialize: function ( options ) {
			this.cupId = options.options.cupId;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			var model = this.model.toJSON();
			console.log( model );

			this.trigger( 'navigation:set:active:cup', { selectedCupId: this.cupId } );

			var data = _.extend( {}, model, { translator: translator } );

			this.$el.html( template( data ) );

			return this;
		}
	} );
} );