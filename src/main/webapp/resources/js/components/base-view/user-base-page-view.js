define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var userPageTemplate = _.template( require( 'text!./templates/user-base-page-template.html' ) );

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

		renderBody: function() {

			this.$( '.js-body-view-container' ).html( userPageTemplate( {
			} ) );

			this.bodyView = this.bodyRenderer( this.$( '.js-custom-view' ), this.options ).view();
			this.bodyView.on( 'navigation:set:active:cup', this._setActiveCup, this );
		},

		_setActiveCup: function( options ) {
			this.headerView.trigger( 'navigation:set:active:cup', options );
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
