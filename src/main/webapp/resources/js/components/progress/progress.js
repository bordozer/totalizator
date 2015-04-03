define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	return Backbone.View.extend({

		initialize: function( options ) {
		},

		render: function ( el, defaultIconClass ) {

			this.el = el;
			this.defaultIconClass = defaultIconClass;

			this.el.removeClass( defaultIconClass );
			this.el.addClass( 'fa-spinner fa-spin' );

			return this;
		},

		close: function() {
			this.el.removeClass( 'fa-spin' );
			this.el.removeClass( 'fa-spinner' );

			this.el.addClass( this.defaultIconClass );
		}
	});
} );
