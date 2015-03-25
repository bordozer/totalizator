define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var PageView = require( 'js/base/base-page-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Menu: Admin"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	return PageView.extend( {

		builtinEvents: {
		},

		mainMenuItems: function() {
			return [
				{ selector: '', icon: 'fa fa-cog', link: '/admin/', text: translator.menuAdminLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		}
	});
} );
