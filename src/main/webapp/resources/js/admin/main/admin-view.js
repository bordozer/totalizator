define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/main/templates/admin-template.html' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'js/translator' );
	var translator = new Translator( {
		title: 'Totalizator'
	});

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
				user: this.model.get( 'userDTO' )
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

