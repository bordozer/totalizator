define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/cup-users-scores-widget-template.html' ) );

	var WidgetView = require( 'js/components/widget/widget-view' );

	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		title: 'Cup users scores: Scores'
		, userColumn: 'Cup users scores: User name'
		, pointsColumn: 'Cup users scores: Points'
		, removeUserGroupFilter: "Remove user group filter"
	} );

	return WidgetView.extend( {

		events: {
			'click .js-user-group': '_filterByUserGroup'
		},

		initialize: function ( options ) {
			this.user = options.user;

			this.listenTo( this.model, 'sync', this._renderScores );
			this.render();
		},

		renderBody: function () {
			this.model.fetch( { cache: false } );
		},

		getTitle: function () {
			return this.getCupTitle( this.model.cup, translator.title );
		},

		getIcon: function () {
			return 'fa-sort-amount-desc';
		},

		getCustomMenuItems: function() {
			return this._userGroupMenuItems( this.user.userId );
		},

		_renderScores: function () {

			var data = _.extend( {}, this.model.toJSON(), { translator: translator } );

			this.setBody( template( data ) );

			this.trigger( 'inner-view-rendered' );

			return this;
		},

		_filterByUserGroup: function( evt ) {
			var menu = $( evt.target );
			var userGroupId = menu.data( 'entity_id' );
			this.model.userGroupId = userGroupId ? userGroupId : 0;

			this.render();
		},

		_userGroupMenuItems: function ( userId ) {

			var ownGroupsMenuItems = this._ownUserGroupMenuItems( userId );
			var anotherGroupMenuItems = this._anotherUserGroupMenuItems( userId );

			var userIsNotAMemberOfAGroups = ownGroupsMenuItems.length == 0 && anotherGroupMenuItems.length == 0;
			if ( userIsNotAMemberOfAGroups ) {
				return [];
			}

			var menuItems = [];

			if ( this.model.userGroupId > 0 ) {
				menuItems.push( { selector: 'divider' } );
				menuItems.push( { selector: 'js-user-group', icon: 'fa fa-filter', link: '#', text: translator.removeUserGroupFilter } );
			}

			menuItems = menuItems.concat( ownGroupsMenuItems );
			menuItems = menuItems.concat( anotherGroupMenuItems );

			return menuItems;
		},

		_ownUserGroupMenuItems: function ( userId ) {

			var userGroupsForCup = this._filterUserGroupsByCup( service.loadUserGroupsWhereUserIsOwner( userId ) );

			if ( userGroupsForCup.length == 0 ) {
				return [];
			}

			var menuItems = [
				{ selector: 'divider' }
			];

			var selectedUserGroupId = this.model.userGroupId;

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

			var menuItems = [
				{ selector: 'divider' }
			];

			var selectedUserGroupId = this.model.userGroupId;

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

			var cupId = this.model.cup.cupId;

			return _.filter( userGroups, function( userGroup ) {
				return _.filter( userGroup.userGroupCups, function( cup ) {
					return cup.cupId == cupId;
				} ).length > 0;
			} );
		}
	} );
} );
