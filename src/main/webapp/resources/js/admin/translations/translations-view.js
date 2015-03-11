define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/admin/translations/templates/translations-template.html' );

	var AdminView = Backbone.View.extend( {

		template:_.template( Template ),

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				userName: this.model.get( 'userName' )
				, untranslatedList: this.model.get( 'untranslatedList' )
			 } ) );

			return this.$el;
		}
	} );

	return { AdminView: AdminView };
} );

