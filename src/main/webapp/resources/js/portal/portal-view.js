define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/portal/templates/portal-template.html' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		portalPageTitle: "Portal page title"
		, adminLinkLabel: "Menu: Admin"
		, logoutLabel: 'Menu: Logout'
	} );

	var PortalPageView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.renderHeader();

			this.$el.html( this.template( {
				userName: this.model.get( 'userName' )
				, translator: translator
			 } ) );

			return this.$el;
		},

		renderHeader: function() {
			var container = this.$( '.admin-menu-container');

		},

		menus: function() {
			return [
				{ selector: '', icon: 'fa fa-cog', link: '/admin/', text: translator.adminLinkLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.logoutLabel }
			];
		},

		_logout: function() {
			Services.logout();
		}

	} );

	return { PortalPageView: PortalPageView };
} );

