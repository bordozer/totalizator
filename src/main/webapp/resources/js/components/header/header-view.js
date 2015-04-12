define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/header-template.html' ) );

	var CupsNavigation = require( 'js/components/cups-navi/cups-navi' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		projectNameLabel: 'Project name: Totalizator'
	} );

	var HeaderView = Backbone.View.extend( {

		initialize: function( options ) {
			this.breadcrumbs = options.options.breadcrumbs;

			this.on( 'navigation:set:active:cup', this._setActiveCup, this );

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

			this.cupsNavigation = new CupsNavigation( 0, this.$( '.js-cups-navi' ) ).view();

			return this;
		},

		_setActiveCup: function( options ) {
			this.cupsNavigation.trigger( 'navigation:set:active:cup', options );
		}
	} );

	return { HeaderView: HeaderView };
} );

