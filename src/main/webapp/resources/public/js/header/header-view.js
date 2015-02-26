define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!public/js/header/templates/header-template.html' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		projectNameLabel: 'Project name: Totalizator'
	} );

	var HeaderView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.subtitle = options.subtitle;

			this.render();
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
				, subtitle: this.subtitle
				, translator: translator
			} ) );

			return this;
		}
	} );

	return { HeaderView: HeaderView };
} );
