define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/component-user-groups-template.html' ) );

	var app = require( 'app' );
	var service = require( '/resources/js/services/service.js' );

	var chosen = require( 'chosen' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userGroupLabel: "User group"
		, noFilterByUserGroupsLabel: "No filter by user groups"
		, removeUserGroupFilter: "Remove user group filter"
	} );

	return Backbone.View.extend( {

		events: {
			'change #selectedUserGroupId': '_onUserGroupSelect'
		},

		initialize: function ( options ) {
			this.selectedUserGroupId = options.selectedUserGroupId || 0;
			this.cup = options.cup;
		},

		render: function () {

			var data = _.extend( {}, { userGroups: service.loadUserGroups(), selectedUserGroupId: this.selectedUserGroupId, translator: translator } );

			this.$el.html( template( data ) );

			var options = {
				width: "100%"
			};
			this.$( '#selectedUserGroupId' ).chosen( options );

			return this;
		},

		getMenuItems: function() {
			return this._userGroupMenuItems( app.currentUser().userId );
		},

		_userGroupMenuItems: function ( userId ) {

			var separator = { selector: 'divider' };

			var ownGroupsMenuItems = this._ownUserGroupMenuItems( userId );
			var anotherGroupMenuItems = this._anotherUserGroupMenuItems( userId );

			var userIsNotAMemberOfAGroups = ownGroupsMenuItems.length == 0 && anotherGroupMenuItems.length == 0;
			if ( userIsNotAMemberOfAGroups ) {
				return [];
			}

			var menuItems = [];

			if ( this.selectedUserGroupId > 0 ) {
				menuItems.push( { selector: 'js-user-group', icon: 'fa fa-filter', link: '#', text: translator.removeUserGroupFilter } );
				menuItems.push( separator );
			}

			menuItems = menuItems.concat( ownGroupsMenuItems );

			if ( ownGroupsMenuItems.length > 0 && anotherGroupMenuItems.length > 0 ) {
				menuItems = menuItems.concat( [ separator ] );
			}

			menuItems = menuItems.concat( anotherGroupMenuItems );

			return menuItems;
		},

		_ownUserGroupMenuItems: function ( userId ) {

			var userGroupsForCup = this._filterUserGroupsByCup( service.loadUserGroupsWhereUserIsOwner( userId ) );

			if ( userGroupsForCup.length == 0 ) {
				return [];
			}

			var menuItems = [];

			var selectedUserGroupId = this.selectedUserGroupId;

			_.each( userGroupsForCup, function( userGroup ) {
				menuItems.push( {
					selector: 'js-user-group'
					, icon: 'fa fa-group'
					, link: '#'
					, text: userGroup.userGroupName
					, entity_id: userGroup.userGroupId
					, selected: selectedUserGroupId == userGroup.userGroupId
				} );
			});

			return menuItems;
		},

		_anotherUserGroupMenuItems: function ( userId ) {

			var userGroups = this._filterUserGroupsByCup( service.loadUserGroupsWhereUserIsMember( userId ) );

			if ( userGroups.length == 0 ) {
				return [];
			}

			var menuItems = [];

			var selectedUserGroupId = this.selectedUserGroupId;

			_.each( userGroups, function( userGroup ) {
				var groupName = userGroup.userGroupName + ' ( ' + userGroup.userGroupOwner.userName + ' )';
				menuItems.push( {
					selector: 'js-user-group'
					, icon: 'fa fa-group'
					, link: '#'
					, text: groupName
					, entity_id: userGroup.userGroupId
					, selected: selectedUserGroupId == userGroup.userGroupId
				} );
			});

			return menuItems;
		},

		_filterUserGroupsByCup: function( userGroups ) {

			var cupId = this.cup.cupId;

			return _.filter( userGroups, function( userGroup ) {
				return _.filter( userGroup.userGroupCups, function( cup ) {
					return cup.cupId == cupId;
				} ).length > 0;
			} );
		},

		_onUserGroupSelect: function() {
			this.selectedUserGroupId = $( '#selectedUserGroupId' ).val();
		}
	} );
} );