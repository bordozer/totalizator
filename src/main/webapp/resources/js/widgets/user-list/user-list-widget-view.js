define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-list-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: "Users"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-user-group': '_onUserGroupButtonClick'
		},

		initialize: function( options ) {

			this.currentUser = options.options.currentUser;

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

			var data = _.extend( {}, { users: users
				, currentUser: this.currentUser
				, _userGroupsMatrix: this._userGroupsMatrix( users )
				, translator: translator
			} );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );
		},

		_userGroupsMatrix: function( users ) {

			var result = {};

			//var self = this;
			_.each( users, function( user ) {

				var userId = user.userId;

				var memberOfGroups = service.loadMemberGroups( userId );

				var userGroups = [];
				_.each( memberOfGroups, function( userGroup ) {
					userGroups.push( { userGroup: userGroup } );
				});

				result[ userId ] = userGroups;
			});

			//var ownGroups = service.loadOwnerGroups( this.currentUser.userId );
			//var memberOfGroups = service.loadOwnerGroups( this.currentUser.userId );
			console.log( result );
			return result;
		},

		_onUserGroupButtonClick: function( evt ) {

			var button = $( evt.target );

			var userId = button.data( 'user-id' );
			var groupId = button.data( 'group-id' );

			//console.log( userId, groupId );

			// TODO: process user group
			// service.addUserToGroup( userId, groupId );
			// service.removeUserFromGroup( userId, groupId );
			// service.cancelUserInvitationToGroup( userId, groupId );
		}
	});
} );
