define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var userPageTemplate = _.template( require( 'text!./templates/user-base-page-template.html' ) );

	var PageView = require( 'js/components/base-view/base-page-view' );

	var app = require( 'app' );
	var service = require( '/resources/js/services/service.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Administration"
		, menuUsersLabel: 'Users'
		, menuYourGroupsLabel: 'Your groups'
		, menuPersonalDataLabel: 'User settings'
		, menuLogoutLabel: 'Menu: Logout'
		, favoritesLabel: 'Favorites'
	} );

	return PageView.extend( {

		builtinEvents: {
			'click .logout-link': 'logout'
		},

		renderBody: function() {

			this.$( '.js-body-view-container' ).html( userPageTemplate() );

			this.bodyView = this.bodyRenderer( this.$( '.js-custom-view' ), this.options ).view();
			this.bodyView.on( 'navigation:set:active:cup', this._setActiveCup, this );
		},

		_setActiveCup: function( options ) {
			this.headerView.trigger( 'navigation:set:active:cup', options );
		},

		mainMenuItems: function() {

			var menus = [];

			var currentUser = app.currentUser();

			if ( service.isAdmin( currentUser ) ) {
				menus.push( { selector: '', icon: 'fa fa-cogs', link: '/admin/', text: translator.menuAdminLabel } );
				menus.push( { selector: 'divider' } );
			}

			menus.push( { selector: '', icon: 'fa fa-star', link: '/betmen/favorites/', text: translator.favoritesLabel } );
			menus.push( { selector: '', icon: 'fa fa-users', link: '/betmen/users/' + currentUser.userId + '/groups/', text: translator.menuYourGroupsLabel } );
			menus.push( { selector: '', icon: 'fa fa-database', link: '/betmen/users/' + currentUser.userId + '/settings/', text: translator.menuPersonalDataLabel } );
			menus.push( { selector: 'divider' } );

			menus.push( { selector: '', icon: 'fa fa-user', link: '/betmen/users/', text: translator.menuUsersLabel } );

			menus.push( { selector: 'divider' } );
			menus.push( { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel } );

			return menus;
		}
	});
} );
