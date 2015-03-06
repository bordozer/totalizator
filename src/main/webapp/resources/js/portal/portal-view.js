define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/portal/templates/portal-template.html' );

	var PageView = require( 'js/base/base-page-view' );

	var MatchesModel = require( 'js/matches/matches-model' );
	var MatchesView = require( 'js/matches/matches-view' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		pageTitle: "Portal page title"
		, menuAdminLabel: "Menu: Admin"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var PortalPageView = PageView.extend( {

		template:_.template( Template ),

		events: {
			'click .logout-link': '_logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		getPageSubTitle: function() {
			return translator.pageTitle;
		},

		renderBody: function ( el ) {

			el.html( this.template( {
				userName: this.model.get( 'userName' )
				, translator: translator
			 } ) );

			this._renderMatches();

			return this;
		},

		mainMenuItems: function() {
			return [
				{ selector: '', icon: 'fa fa-cog', link: '/admin/', text: translator.menuAdminLabel }
				, { selector: 'divider' }
				, { selector: 'logout-link', icon: 'fa fa-sign-out', link: '#', text: translator.menuLogoutLabel }
			];
		},

		_renderMatches: function() {
			var matchesModel = new MatchesModel.MatchesModel();
			var matchesView = new MatchesView.MatchesView( { model: matchesModel, el: this.$( '.portal-page-matches-container' ) } );
		},

		_logout: function() {
			Services.logout();
		}

	} );

	return { PortalPageView: PortalPageView };
} );

