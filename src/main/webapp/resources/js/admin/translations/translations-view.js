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
		, menuPortalPage: 'Menu: Back to site'
		, menuAdmin: 'Menu: Admin'
		, reloadTranslations: 'Menu: Reload translations'
		, menuLogout: 'Menu: Logout'
	} );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .admin-reload-translations': '_reloadTranslations'
			, 'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				userName: this.model.get( 'userName' )
				, untranslatedList: this.model.get( 'untranslatedList' )
				, translator: translator
			 } ) );

			return this.$el;
		},

		_reloadTranslations: function() {
			Services.reloadTranslations();
		},

		_logout: function() {
			Services.logout();
		}

	} );

	return { AdminView: AdminView };
} );

