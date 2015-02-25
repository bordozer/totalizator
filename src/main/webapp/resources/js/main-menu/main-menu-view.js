define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/main-menu/templates/main-menu-template.html' );

	var MainMenuView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.menus = options.menus;
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model.toJSON()
				, menus: this.menus
			 } ) );

			return this;
		}

	} );

	return { MainMenuView: MainMenuView };
} );

