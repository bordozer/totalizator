define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/translations/templates/translations-template.html' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'public/js/translator' );
	var translator = new Translator( {
		pageTitle: 'Translations: Page Title'
		, menuLogout: 'Menu: Logout'
		, menuAdmin: 'Menu: Admin'
		, menuPortalPage: 'Menu: Back to site'
	} );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				untranslatedList: this.model.get( 'untranslatedList' )
				, translator: translator
			 } ) );

			return this.$el;
		},

		_logout: function() {
//			if ( confirm( 'Logout?' ) ) {
				Services.logout();
//			}
		}

	} );

	return { AdminView: AdminView };
} );

