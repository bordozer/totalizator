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
			this.breadcrumbs = options.breadcrumbs;

			this.render();
		},

		render: function () {

			var title = this.breadcrumbs[ this.breadcrumbs.length - 1 ].title;

			this.$el.html( template( {
				model: this.model
				, title: title
				, breadcrumbs: this.breadcrumbs
				, translator: translator
			} ) );

			return this;
		}
	} );

	return { HeaderView: HeaderView };
} );

