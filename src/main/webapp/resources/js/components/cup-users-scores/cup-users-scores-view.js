define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-template.html' ) );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.listenTo( this.model, 'sync', this.render );
			this.model.fetch( { cache: false} );
		},

		render: function () {

			var data = _.extend( {}, this.model.toJSON() );

			this.$el.html( template( data ) );

			return this;
		}
	} );
} );
