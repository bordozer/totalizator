define( function ( require ) {

	'use strict';

	var _ = require( 'underscore' );

	var service = require( '/resources/js/services/service.js' );

	var Model = require( './cup-users-scores-widget-model' );
	var View = require( './cup-users-scores-widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		removeUserGroupFilter: "Remove user group filter"
	} );

	function init( container, options ) {

		// TODO: widget menu 'Refresh' does not update user groups menus
		var menuItems = userGroupMenuItems( options.user.userId );

		var model = new Model( { cup: options.cup } );
		var view = new View( {
			model: model
			, el: container
			, menuItems: menuItems
		} );
	}

	function userGroupMenuItems( userId ) {

		var ownGroupsMenuItems = ownUserGroupMenuItems( userId );
		var anotherGroupMenuItems = anotherUserGroupMenuItems( userId );

		var userIsNotAMemberOfAGroups = ownGroupsMenuItems.length == 0 && anotherGroupMenuItems.length == 0;
		if ( userIsNotAMemberOfAGroups ) {
			return [];
		}

		var menuItems = [
			{ selector: 'divider' }
			, { selector: 'js-user-group', icon: 'fa fa-filter', link: '#', text: translator.removeUserGroupFilter }
		];

		menuItems = menuItems.concat( ownGroupsMenuItems );
		menuItems = menuItems.concat( anotherGroupMenuItems );

		return menuItems;
	}

	function ownUserGroupMenuItems( userId ) {

		var userGroups = service.loadUserGroupsWhereUserIsOwner( userId );

		if ( userGroups.length == 0 ) {
			return [];
		}

		var menuItems = [
			{ selector: 'divider' }
		];

		_.each( userGroups, function( userGroup ) {
			menuItems.push( { selector: 'js-user-group', icon: 'fa fa-group', link: '#', text: userGroup.userGroupName, entity_id: userGroup.userGroupId } );
		});

		return menuItems;
	}

	function anotherUserGroupMenuItems( userId ) {

		var userGroups = service.loadUserGroupsWhereUserIsMember( userId );

		if ( userGroups.length == 0 ) {
			return [];
		}

		var menuItems = [
			{ selector: 'divider' }
		];

		_.each( userGroups, function( userGroup ) {
			var groupName = userGroup.userGroupName + ' ( ' + userGroup.userGroupOwner.userName + ' )';
			menuItems.push( { selector: 'js-user-group', icon: 'fa fa-group', link: '#', text: groupName, entity_id: userGroup.userGroupId } );
		});

		return menuItems;
	}

	return init;
} );
