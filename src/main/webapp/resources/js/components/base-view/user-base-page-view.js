define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var PageView = require( 'js/components/base-view/base-page-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Menu: Admin"
		, menuUsersLabel: 'Users'
		, menuLogoutLabel: 'Menu: Logout'
	} );

	return PageView.extend( {

		builtinEvents: {
			'click .logout-link': 'logout'
		},

		mainMenuItems: function() {
			return [
				{ selector: '', icon: 'fa fa-cogs', link: '/admin/', text: translator.menuAdminLabel }
				, { selector: 'divider' }
				, { selector: '', icon: 'fa fa-users', link: '/totalizator/users/', text: translator.menuUsersLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		}
	});
} );
