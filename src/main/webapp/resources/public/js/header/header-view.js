define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!public/js/header/templates/header-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		projectNameLabel: 'Project name: Totalizator'
	} );

	var HeaderView = Backbone.View.extend( {

		initialize: function( options ) {
			this.subtitle = options.subtitle;

			this.render();
		},

		render: function () {

			this.$el.html( template( {
				model: this.model
				, subtitle: this.subtitle
				, translator: translator
			} ) );

			return this;
		}
	} );

	return { HeaderView: HeaderView };
} );

