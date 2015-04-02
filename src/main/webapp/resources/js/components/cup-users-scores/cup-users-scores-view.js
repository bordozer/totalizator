define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-template.html' ) );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.render();
		},

		render: function () {

			this.$el.html( template( {
			} ) );

			return this;
		}
	} );
} );
