define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/portal-template.html' ) );

	var MatchesModel = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-model' );
	var MatchesView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-view' );

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

			this._renderMatches();

			return this;
		},

		_renderMatches: function() {

			var currentUser = this.currentUser;

			var el = this.$( '.js-portal-page-container' );

			_.each( this.cupsToShow, function( cup ) {

				var container = $( '<div class="col-xs-6 col-sm-6 col-md-6 col-lg-4"></div>' );
				el.append( container );

				var model = new MatchesModel.MatchesModel();

				var view = new MatchesView.MatchesView( {
					model: model
					, el: container
					, settings: {
						categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, showFutureMatches: true
					}
					, menuItems: []
					, currentUser: currentUser
				} );
			} );
		}
	} );

	return { PortalPageView: PortalPageView };
} );

