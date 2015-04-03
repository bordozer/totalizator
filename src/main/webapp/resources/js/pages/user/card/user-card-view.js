define( function ( require ) {

	'use strict';

	var Backbone = require( 'backbone' );
	var _ = require( 'underscore' );
	var $ = require( 'jquery' );

	var template = _.template( require( 'text!./templates/user-card-template.html' ) );

	var Translator = require( 'translator' );
	var translator = new Translator( {
		userStatisticsLabel: "User card: Statistics"
		, userBetsLabel: "User card: User bets"
	} );

	return Backbone.View.extend( {

		initialize: function( options ) {
			this.userId = options.options.userId;

			this.render();
		},

		render: function () {

			this.$el.html( template( {
				translator: translator
			} ) );

			this._renderUserStatistics();

			this._renderUserBets();
		},

		_renderUserStatistics: function() {
			this.$( '.js-user-statistics' ).html( "User's statistics are going to be here..." );
		},

		_renderUserBets: function() {
			this.$( '.js-user-bets' ).html( '' );
		}
	});
} );
