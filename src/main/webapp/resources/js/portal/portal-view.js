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
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				userName: this.model.get( 'userName' )
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

