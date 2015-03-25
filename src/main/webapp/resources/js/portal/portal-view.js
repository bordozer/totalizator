define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!js/portal/templates/portal-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Menu: Admin"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var PortalPageView = Backbone.View.extend( {

		builtinEvents: {
			'click .logout-link': 'logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				cupsShowTo: this.model.get( 'cupsShowTo' )
				, translator: translator
			 } ) );

			return this;
		}
	} );

	return { PortalPageView: PortalPageView };
} );

