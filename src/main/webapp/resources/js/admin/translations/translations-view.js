define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/translations/templates/translations-template.html' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: 'Translations: Page Title'
	} );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		getPageSubTitle: function() {
			return translator.pageTitle;
		},

		render: function () {

			this.$el.html( this.template( {
				userName: this.model.get( 'userName' )
				, untranslatedList: this.model.get( 'untranslatedList' )
				, translator: translator
			 } ) );

			return this.$el;
		}
	} );

	return { AdminView: AdminView };
} );

