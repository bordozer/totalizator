define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-widget-template.html' ) );
	var EntryView = require( './user-list-widget-entry-view' );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Users"
		, addUserToGroup: "Add user to group"
		, removeUserFromGroup: "Remove user from group"
		, addUserToGroupConfirmation: "Add the user to the group?"
		, removeUserFromGroupConfirmation: "Remove the user from the group?"
	} );

	return WidgetView.extend( {

		initialize: function( options ) {

			this.currentUser = options.options.currentUser;

			//this.users = service.loadUsers();

			this.listenTo( this.model, 'sync', this._renderUserList );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return translator.title;
		},

		getIcon: function () {
			return 'fa-users';
		},

		_renderUserList: function() {

			var users = this.model.toJSON();

			var currentUser = this.currentUser;
			var currentUserGroups = service.loadUserGroupsWhereUserIsOwner( this.currentUser.userId );

			var data = _.extend( {}, {
				currentUserGroups: currentUserGroups
				, translator: translator
			} );

			this.setBody( template( data ) );
			var container = this.$( '.js-users-container' );

			_.each( users, function( user ) {

				var el = $( '<div></div>' );
				container.append( el );

				var view = new EntryView( {
					el: el
					, user: user
					, currentUser: currentUser
					, currentUserGroups: currentUserGroups
					, users: users
				} );
			});

			this.trigger( 'inner-view-rendered' );
		}

		/*_userGroupsMatrix: function( users ) {

			var result = {};

			_.each( users, function( user ) {

				var userId = user.userId;

				var memberOfGroups = service.loadUserGroupsWhereUserIsMember( userId );

				var userGroups = [];
				_.each( memberOfGroups, function( userGroup ) {
					userGroups[ userGroup.userGroupId ] = true;
				});

				result[ userId ] = userGroups;
			});

			return result;
		}*/

		/*_onAddUserToGroupClick: function( evt ) {

			var data = this._getData( $( evt.target ) );

			var user = data.user;
			var userGroup = data.userGroup;

			/!*if ( ! confirm( this._confirmMessage( translator.addUserToGroupConfirmation, data ) ) ) {
				return;
			}*!/

			service.addUserToUserGroup( user.userId, userGroup.userGroupId );

			this.render();
		},

		_onRemoveUserFromGroupClick: function( evt ) {

			var data = this._getData( $( evt.target ) );

			var user = data.user;
			var userGroup = data.userGroup;

			/!*if ( ! confirm( this._confirmMessage( translator.removeUserFromGroupConfirmation, data ) ) ) {
				return;
			}*!/

			service.removeUserFromGroup( user.userId, userGroup.userGroupId );

			this.render();
		},*/

		/*_getData: function( button ) {

			var userId = button.data( 'user-id' );
			var userGroupId = button.data( 'group-id' );

			var user = _.find( this.users, function( user ) {
				return user.userId == userId;
			} );

			var userGroup = _.find( this.currentUserGroups, function( userGroup ) {
				return userGroup.userGroupId == userGroupId;
			} );

			return { user: user, userGroup: userGroup };
		},*/

		/*_confirmMessage: function( message, data ) {

			var user = data.user;
			var userGroup = data.userGroup;

			return message + "\n\nUser: " + user.userName + "\nUser group: " + userGroup.userGroupName; // TODO: translate
		}*/
	});
} );
