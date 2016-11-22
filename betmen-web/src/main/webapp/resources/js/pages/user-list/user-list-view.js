define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );

	var template = _.template( require( 'text!./templates/user-list-template.html' ) );

	var UserListWidget = require( 'js/widgets/user-list/user-list-widget' );

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
