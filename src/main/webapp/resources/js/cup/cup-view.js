define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!js/portal/templates/portal-template.html' ) );

	var MatchesModel = require( 'js/matches/matches-model' );
	var MatchesView = require( 'js/matches/matches-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		menuAdminLabel: "Menu: Admin"
		, menuLogoutLabel: 'Menu: Logout'
	} );

	var CupPageView = Backbone.View.extend( {

		cupId: 0,

		builtinEvents: {
			'click .logout-link': 'logout'
		},

		initialize: function( options ) {
			this.cupId = options.options.cupId;

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

			var el = this.$( '.js-cup-page-matches' );

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
						, teamId: 0
					}
					, menuItems: []
				} );
			} );
		}
	} );

	return { CupPageView: CupPageView };
} );

