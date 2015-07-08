define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-widget-entry-template.html' ) );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Users"
	} );

	return Backbone.View.extend( {

		events: {
			'click .js-add-user-to-group': '_onAddUserToGroupClick'
			, 'click .js-remove-user-from-group': '_onRemoveUserFromGroupClick'
		},

		initialize: function( options ) {

			this.user = options.user;
			this.currentUser = options.currentUser;
			this.currentUserGroups = options.currentUserGroups;
			this.users = options.users;

			this.render();
		},

		render: function () {

			this.memberOfGroups = service.loadUserGroupsWhereUserIsMember( this.user.userId );

			var data = _.extend( {}, {
				user: this.user
				, currentUser: this.currentUser
				, currentUserGroups: this.currentUserGroups
				, isMember: this._isMember.bind( this )
				, translator: translator
			} );

			this.$el.html( template( data ) );
		},

		_isMember: function( userGroupId ) {

			var result = _.filter( this.memberOfGroups, function( userGroup ) {
				return userGroup.userGroupId == userGroupId;
			} );

			return result.length > 0;
		},

		_onAddUserToGroupClick: function( evt ) {

			var data = this._getData( $( evt.target ) );

			var user = data.user;
			var userGroup = data.userGroup;

			service.addUserToUserGroup( user.userId, userGroup.userGroupId );

			this.render();
		},

		_onRemoveUserFromGroupClick: function( evt ) {

			var data = this._getData( $( evt.target ) );

			var user = data.user;
			var userGroup = data.userGroup;

			service.removeUserFromGroup( user.userId, userGroup.userGroupId );

			this.render();
		},

		_getData: function( button ) {

			var userId = button.data( 'user-id' );
			var userGroupId = button.data( 'group-id' );

			var user = _.find( this.users, function( user ) {
				return user.userId == userId;
			} );

			var userGroup = _.find( this.currentUserGroups, function( userGroup ) {
				return userGroup.userGroupId == userGroupId;
			} );

			return { user: user, userGroup: userGroup };
		}
	});
} );

