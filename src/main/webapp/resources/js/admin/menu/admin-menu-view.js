define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/menu/templates/admin-menu-template.html' );

	var AdminMenuView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.render( options.menus );
		},

		render: function ( menus ) {

			this.$el.html( this.template( {
				model: this.model.toJSON()
				, menus: menus
			 } ) );

			return this.$el;
		}

	} );

	return { AdminMenuView: AdminMenuView };
} );

