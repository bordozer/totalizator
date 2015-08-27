define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-data-edit-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userName: "User name"
	} );

	return Backbone.View.extend( {

		events: {
			'change #userName': '_bind'
		},

		initialize: function( options ) {
		},

		render: function() {
			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );
			this.$el.html( template( data ) );

			this.$( '#userName' ).focus().select();
		},

		_bind: function () {
			this.model.set( { userName: this.$( '#userName' ).val() } );
		}
	});
} );


