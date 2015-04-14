define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!./templates/main-menu-template.html' );

	var MainMenuView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.menus = options.menus;
			this.icon = options.icon;
			this.cssClass = options.cssClass;
		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model.toJSON()
				, icon: this.icon
				, menus: this.menus
				, cssClass: this.cssClass
			 } ) );

			return this;
		}

	} );

	return { MainMenuView: MainMenuView };
} );

