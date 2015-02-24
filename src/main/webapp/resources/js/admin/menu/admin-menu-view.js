define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/menu/templates/admin-menu-template.html' );

	var Translator = require( 'public/js/translator' );
	var translator = new Translator( {
	} );

	var AdminMenuView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.render( options.menus );
		},

		render: function ( menus ) {

			this.$el.html( this.template( {
				model: this.model.toJSON()
				, menus: menus
				, translator: translator
			 } ) );

			return this.$el;
		}

	} );

	return { AdminMenuView: AdminMenuView };
} );

