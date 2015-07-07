define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-template.html' ) );

	var UserListWidget = require( 'js/widgets/user-list/user-list-widget' );

	var service = require( '/resources/js/services/service.js' );

	return Backbone.View.extend( {

		initialize: function( options ) {

			this.currentUser = options.options.currentUser;

			this.render();
		},

		render: function() {

			this.$el.html( template() );

			var userListWidget = new UserListWidget( this.$( '.js-user-list' ), { currentUser: this.currentUser } );
		}
	});
} );
