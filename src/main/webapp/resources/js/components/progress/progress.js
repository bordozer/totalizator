define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/progress-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		loadingLabel: 'Loading...'
	} );

	return Backbone.View.extend({

		initialize: function( options ) {
			this.render();
		},

		render: function () {
			this.$el.html( template( translator ) );
			return this;
		},

		close: function() {
			this.remove();
		}
	});
} );
