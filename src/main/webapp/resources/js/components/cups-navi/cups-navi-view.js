define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/caps-navi-template.html' ) );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.selectedCupId = options.selectedCupId;

			this.on( 'navigation:set:active:cup', this.render, this );

			this.listenTo( this.model, 'sync', this.render );
			this.model.fetch( { cache: false} );
		},

		render: function () {

			this.$el.html( template( {
				cupsShowTo: this.model.toJSON()
				, selectedCupId: this.selectedCupId
			} ) );

			return this;
		},

		_setSelectedCupId: function( options ) {

			console.log( '_setSelectedCupId', options );

			this.selectedCupId = options.selectedCupId;

			this.render();
		}
	} );
} );
