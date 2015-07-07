define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Users"
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-user-group': '_onUserGroupButtonClick'
		},

		initialize: function( options ) {

			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function() {

			this.$el.html( template( {
				users: this.model.toJSON()
				, currentUser: this.currentUser
				, userGroups: service.loadUserGroups( this.currentUser.userId )
				, translator: translator
			} ) );
		},

		_onUserGroupButtonClick: function( evt ) {

			var button = $( evt.target );

			var userId = button.data( 'user-id' );
			var groupId = button.data( 'group-id' );

			// TODO: process user group
		}
	});
} );
