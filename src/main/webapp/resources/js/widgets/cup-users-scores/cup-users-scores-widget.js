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

		var userGroups = service.loadUserGroupsWhereUserIsOwner( options.user.userId );

		var menuItems = [
			{ selector: 'divider' }
			, { selector: 'js-user-group', icon: 'fa fa-filter', link: '#', text: translator.removeUserGroupFilter }
			, { selector: 'divider' }
		];

		_.each( userGroups, function( userGroup ) {
			menuItems.push( { selector: 'js-user-group', icon: 'fa fa-group', link: '#', text: userGroup.userGroupName, entity_id: userGroup.userGroupId } );
		});

		var model = new Model( { cup: options.cup } );
		var view = new View( {
			model: model
			, el: container
			, menuItems: menuItems
		} );
	}

	return init;
} );
