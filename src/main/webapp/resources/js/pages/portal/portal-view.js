define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/portal-template.html' ) );

	var CupsNaviView = require( 'js/components/cups-navi/cups-navi' );

	var MatchesModel = require( 'js/components/matches-and-bets/matches-and-bets-model' );
	var MatchesView = require( 'js/components/matches-and-bets/matches-and-bets-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Menu: Admin"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var PortalPageView = Backbone.View.extend( {

		initialize: function( options ) {
			this.cupsToShow = options.options.cupsToShow;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			 } ) );

			this._renderNavigation();

			this._renderMatches();

			return this;
		},

		_renderNavigation: function() {
			var selectedCupId = 0;
			var cupsNaviView = new CupsNaviView( selectedCupId, this.$( '.js-cups-navi' ) );
		},

		_renderMatches: function() {

			var currentUser = this.currentUser;

			var el = this.$( '.js-portal-page-container' );

			_.each( this.cupsToShow, function( cup ) {

				var container = $( '<div class="col-lg-5"></div>' );
				el.append( container );

				var model = new MatchesModel.MatchesModel();

				var view = new MatchesView.MatchesView( {
					model: model
					, el: container
					, settings: {
						categoryId: cup.categoryId
						, cupId: cup.cupId
						, teamId: 0
					}
					, menuItems: []
					, currentUser: currentUser
				} );
			} );
		}
	} );

	return { PortalPageView: PortalPageView };
} );

