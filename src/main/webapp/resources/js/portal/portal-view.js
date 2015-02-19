define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/portal/templates/portal-template.html' );

	var Services = require( 'js/services.js' );

	var PortalPageView = Backbone.View.extend( {

		template:_.template( Template ),

		events: {
			'click .logout-link': '_logout'
		},

		initialize: function( options ) {

		},

		render: function () {

			this.$el.html( this.template( {
				model: this.model
			 } ) );

			return this.$el;
		},

		_logout: function() {
//			if ( confirm( 'Logout?' ) ) {
				Services.logout();
//			}
		}

	} );

	return { PortalPageView: PortalPageView };
} );

