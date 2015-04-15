define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-card-template.html' ) );

	var MatchesModel = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-model' );
	var MatchesView = require( 'js/widgets/matches-and-bets/matches-and-bets-widget-view' );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userStatisticsLabel: "User card: Statistics"
		, userBetsLabel: "User card: User bets"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.userId = options.options.userId;
			this.currentUser = options.options.currentUser;

			this.model.on( 'sync', this.render, this );
			this.model.fetch( { cache: false } );
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			} ) );

			this._renderUserStatistics();

			this._renderUserBets();
		},

		_renderUserStatistics: function() {
			this.$( '.js-user-statistics' ).html( "User's statistics are going to be here..." ); // TODO: implement as separate component
		},

		_renderUserBets: function() {

			var userId = this.userId;
			var currentUser = this.currentUser;

			var el = this.$( '.js-user-bets' );

			var cupsToShow = this.model.get( 'cupsToShow' );

			_.each( cupsToShow, function( cup ) {

				var container = $( '<div class="col-lg-6"></div>' );
				el.append( container );

				var model = new MatchesModel.MatchesModel();
				var view = new MatchesView.MatchesView( {
					model: model
					, el: container
					, settings: {
						userId: userId
						, categoryId: cup.category.categoryId
						, cupId: cup.cupId
						, teamId: 0
						, showFutureMatches: false
						, showFinished: true
					}
					, menuItems: []
					, currentUser: currentUser
				} );
			});
		}
	});
} );
