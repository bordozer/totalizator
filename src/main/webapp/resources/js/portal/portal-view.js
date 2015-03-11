define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var Template = require( 'text!js/portal/templates/portal-template.html' );

	var MatchesModel = require( 'js/matches/matches-model' );
	var MatchesView = require( 'js/matches/matches-view' );

	var Services = require( '/resources/js/services.js' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Menu: Admin"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var PortalPageView = Backbone.View.extend( {

		template:_.template( Template ),

		builtinEvents: {
			'click .logout-link': 'logout'
		},

		initialize: function( options ) {
			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( this.template( {
				userName: this.model.get( 'userName' )
				, translator: translator
			 } ) );

			this._renderMatches();

			return this;
		},

		_renderMatches: function() {

			var el = this.$( '.portal-page-matches-container' );

			_.each( this.model.get( 'cupsShowTo' ), function( cup ) {

				var container = $( '<div></div>' );
				el.append( container );

				var matchesModel = new MatchesModel.MatchesModel();
				var matchesView = new MatchesView.MatchesView( {
					model: matchesModel
					, el: container
					, settings: {
						categoryId: cup.categoryId
						, cupId: cup.cupId
					}
				} );
			} );
		}
	} );

	return { PortalPageView: PortalPageView };
} );

